#!/bin/bash
# JEECG Boot 前端重建脚本（停止→删除→编译→构建镜像→启动）

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

FRONTEND_CONTAINER="jeecgboot-vue3-nginx"
FRONTEND_IMAGE="jeecgboot-vue3"

echo
echo -e "[1/4] 停止并删除前端容器与镜像..."

# 停止并删除容器
if docker ps -a --format '{{.Names}}' | grep -q "^${FRONTEND_CONTAINER}$"; then
    docker stop  "$FRONTEND_CONTAINER" 2>/dev/null || true
    docker rm -f "$FRONTEND_CONTAINER" 2>/dev/null || true
    echo -e "${GREEN}  容器已删除: ${FRONTEND_CONTAINER}${NC}"
else
    echo "  容器不存在，跳过"
fi

# 删除镜像
if docker images --format '{{.Repository}}' | grep -q "^${FRONTEND_IMAGE}$"; then
    docker rmi -f "$FRONTEND_IMAGE" 2>/dev/null || true
    echo -e "${GREEN}  镜像已删除: ${FRONTEND_IMAGE}${NC}"
else
    echo "  镜像不存在，跳过"
fi

echo -e "[2/4] 安装前端依赖..."
cd "$SCRIPT_DIR/jeecgboot-vue3"
pnpm install

echo -e "[3/4] 编译前端项目 (pnpm run build:docker) + 构建镜像..."
pnpm run build:docker

echo -e "[4/4] 启动前端容器..."
cd "$SCRIPT_DIR"
docker-compose up -d jeecg-vue

echo
echo "========================================"
SERVER_IP=$(hostname -I | awk '{print $1}')
echo -e "  ${GREEN}前端重建成功${NC}"
echo "========================================"
echo "前端访问:      http://${SERVER_IP}"
echo
