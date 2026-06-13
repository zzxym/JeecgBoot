#!/bin/bash
# JEECG Boot 后端重建脚本（停止→删除→编译→构建镜像→启动）

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

BACKEND_CONTAINER="jeecg-boot-system"
BACKEND_IMAGE="jeecg-boot-system"

echo
echo -e "[1/4] 停止并删除后端容器与镜像..."

# 停止并删除容器
if docker ps -a --format '{{.Names}}' | grep -q "^${BACKEND_CONTAINER}$"; then
    docker stop  "$BACKEND_CONTAINER" 2>/dev/null || true
    docker rm -f "$BACKEND_CONTAINER" 2>/dev/null || true
    echo -e "${GREEN}  容器已删除: ${BACKEND_CONTAINER}${NC}"
else
    echo "  容器不存在，跳过"
fi

# 删除镜像
if docker images --format '{{.Repository}}' | grep -q "^${BACKEND_IMAGE}$"; then
    docker rmi -f "$BACKEND_IMAGE" 2>/dev/null || true
    echo -e "${GREEN}  镜像已删除: ${BACKEND_IMAGE}${NC}"
else
    echo "  镜像不存在，跳过"
fi

echo -e "[2/4] 编译后端项目 (mvn clean install -Pdocker)..."
cd "$SCRIPT_DIR/jeecg-boot"
mvn clean install -Pdocker

echo -e "[3/4] 构建 Docker 镜像..."
cd "$SCRIPT_DIR"
docker-compose build --no-cache jeecg-boot-system

echo -e "[4/4] 启动后端容器..."
docker-compose up -d jeecg-boot-system jeecg-boot-mysql jeecg-boot-redis

echo
echo "========================================"
SERVER_IP=$(hostname -I | awk '{print $1}')
echo -e "  ${GREEN}后端重建成功 (请等待约1分钟，待容器启动完成)${NC}"
echo "========================================"
echo "后端API:      http://${SERVER_IP}:8080/jeecg-boot"
echo
