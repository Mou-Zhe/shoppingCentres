
<template>
<div style="width: 70%;margin: 20px auto;min-height: 100vh">
  <div class="cart" style="padding: 20px">
    <div style="margin-bottom: 20px">
      收货地址：
      <el-select style="width: 500px" v-model="data.addressId">
        <el-option v-for="item in data.addressList" :key="item.id" :value="item.id"
                   :label="item.name+'|'+item.phone+'|'+item.address" ></el-option>
      </el-select>
    </div>
    <div>
      <el-table :data="data.cartList" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"/>
        <el-table-column label="商品图片" prop="goodsImg">
          <template #default="scope">
            <el-image @click="router.push('/front/front_goodsDetail?id='+scope.row.goodsId)" style="width: 80px;height: 80px;border-radius: 5px" :src="scope.row.goodsImg"
            :preview-src-list="[scope.row.goodsImg]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column label="商品单价" prop="goodsPrice">
          <template #default="scope">
            <b style="font-size: 20px;color: red">￥{{scope.row.goodsPrice}}</b>
          </template>
        </el-table-column>
        <el-table-column label="商品数量" prop="num">
          <template #default="scope">
            <el-input-number @change="updateCart(scope.row)" :min="1" v-model="scope.row.num" style="width: 150px"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template v-slot="scope">
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <div style="text-align: right">
    <div>总价格:<b style="font-size: 20px;color: red;display:inline-block;min-width: 100px;text-align: left">￥{{data.total}}</b>
    </div><el-button @click="addOrder" size="large" type="danger" style="width: 100px" :disabled="data.total===0">立即下单</el-button>
  </div>
</div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {Delete, Edit} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router/index.js";

const data=reactive({
  user:JSON.parse(localStorage.getItem('xm-user') || '{}'),
  addressId:null,
  addressList:[],
  cartList:[],
  total:0,
  rows:[],//被选择的行
})

const updateCart=(row)=>{
  request.put('/cart/update',row).then(res=>{
    calSum()
  })
}

const calSum=()=>{
  let sum=0
  data.rows.forEach(item=>{
    sum+=item.goodsPrice*item.num;
  })
  data.total=sum.toFixed(2)
}

const handleSelectionChange=(rows)=>{
  data.rows=rows
  calSum()
}

const addOrder=()=>{
  if(!data.addressId){
    ElMessage.warning('请选择收货地址')
    return
  }
  request.post("/orders/add?addressId="+data.addressId,data.rows).then(res=>{
    if(res.code==='200'){
      ElMessage.success('下单成功！')
    }else {
      ElMessage.error(res.msg)
    }
  })
}

//修复：方法没传递id参数
const del=(id)=>{
  ElMessageBox.confirm('订单删除后无法恢复，您确定删除吗？','删除确认',{type:'warning'}).then(res=>{
    request.delete('cart/delete/'+id).then(res=>{
      if(res.code === '200'){
        ElMessage.success('订单删除成功！')
        loadCart()
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch(err=>{
    console.error(err)
  })
}

const loadCart=()=>{
  request.get('/cart/selectAll',{
    params:{
      userId:data.user.id,
    }
  }).then(res=>{
    data.cartList=res.data
  })
}

loadCart()

request.get('/address/selectAll',{
  params:{
    userId:data.user.id,
  }
}).then(res=>{
  data.addressList=res.data
})
</script>

<style>

</style>