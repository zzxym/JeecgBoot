# 订单序号管理 API 文档

> 模块：`org.jeecg.modules.order`  
> 基础路径：`/sys/orderSeq`  
> 认证方式：JWT Token（Header: `X-Access-Token`）

## 数据库表

| 表名 | 说明 |
|------|------|
| `sys_order_seq` | 订单序号表 |

| 字段 | 类型 | 说明 |
|------|------|------|
| `seq_key` | VARCHAR | 主键，序号键（格式：`前缀_YYYYMMDD`，如 `XS_20260613`） |
| `seq_no` | INT | 当前序号值（从 1 开始自增） |

**生成规则**：`{prefix}{YYYYMMDD}{5位序号}`  
例如：`XS2026061300001`

---

## 接口列表

### 1. 获取下一个序号（供 JS 增强调用）

**GET** `/sys/orderSeq/next`

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `prefix` | String | 否 | `CN` | 序号前缀，如 `XS`(销售订单)、`CK`(出库单)、`CG`(采购订单)、`TEST`(测试) |

**响应示例**：
```json
{
  "success": true,
  "message": "操作成功！",
  "result": "XS2026061300001",
  "code": 200,
  "timestamp": 1718240733000
}
```

**前端 JS 增强调用示例**：
```javascript
loaded(){
  this.$nextTick(()=>{
    if(this.isUpdate.value === false){
      getAction('/sys/orderSeq/next', { prefix: 'XS' })
        .then(res=>{
          if(res.success){
            this.setFieldsValue({ order_code: res.result })
          }
        })
    }
  })
}
```

---

### 2. 查询所有序号记录

**GET** `/sys/orderSeq/list`

无参数。

**响应示例**：
```json
{
  "success": true,
  "message": "操作成功！",
  "result": [
    { "seqKey": "XS_20260613", "seqNo": 15 },
    { "seqKey": "TEST_20260613", "seqNo": 3 }
  ],
  "code": 200,
  "timestamp": 1718240733000
}
```

---

### 3. 删除序号记录

**DELETE** `/sys/orderSeq/delete/{seqKey}`

| 参数 | 类型 | 路径/参数 | 必填 | 说明 |
|------|------|-----------|------|------|
| `seqKey` | String | 路径 | 是 | 序号键，如 `XS_20260613` |

**响应示例**：
```json
{
  "success": true,
  "message": "操作成功！",
  "result": "删除成功",
  "code": 200,
  "timestamp": 1718240733000
}
```

---

### 4. 重置序号计数器

**PUT** `/sys/orderSeq/reset/{seqKey}`

将指定序号计数器的 `seq_no` 重置为 0（下次调用 `/next` 时将从 1 开始）。

| 参数 | 类型 | 路径/参数 | 必填 | 说明 |
|------|------|-----------|------|------|
| `seqKey` | String | 路径 | 是 | 序号键，如 `XS_20260613` |

**响应示例**：
```json
{
  "success": true,
  "message": "操作成功！",
  "result": "重置成功",
  "code": 200,
  "timestamp": 1718240733000
}
```

---

## 错误响应

| HTTP 状态码 | code | 说明 |
|-------------|------|------|
| 200 | 200 | 操作成功 |
| 200 | 500 | 业务错误（详见 message） |
| 401 | 401 | Token 为空或无效 |

```json
{
  "success": false,
  "message": "生成序号失败: ...",
  "result": null,
  "code": 500,
  "timestamp": 1718240733000
}
```

---

## 运维清理示例

```bash
# 使用 curl 清空某一天测试数据
curl -X DELETE "http://localhost:8080/jeecg-boot/sys/orderSeq/delete/TEST_20260613" \
  -H "X-Access-Token: YOUR_JWT_TOKEN"
```
