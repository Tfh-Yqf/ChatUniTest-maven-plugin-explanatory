
<template>
    <div style="display: flex;flex-direction: column">
        <a-card>

            <div class="row text-center">
                <!-- <div class="col">
                    <input type="text" class="form-control" v-model="input_text" />
                </div> -->
                <div class="col">
                    <a-popconfirm title="确定执行吗?" @confirm="sendPost">
                        <a-button style="width: 100px;height: 40px" type="primary">数据入库</a-button>
                    </a-popconfirm>
                </div>
                <div class="col">
                    <a-popconfirm title="确定执行吗?" @confirm="sendGet" style="margin-left: 10px;">
                        <a-button style="width: 100px;height: 40px" type="primary">请求数据</a-button>
                    </a-popconfirm>
                </div>
            </div>
        </a-card>
    </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle"
import { sendRequest } from '@/api/system'
import axios from "axios"
import qs from 'qs'
import Q from "q"

const BASE_URL = "http://172.16.100.104:35000"


export default {
    data() {
        return {
            input_text: "",
            initdatas: {
                path: [],
            }

        }
    },
    methods: {
        sendPost() {
            
            // let url='http://172.16.100.104:35000/minio_interface/data_warehousing_single?path=%2Fdfs_data%2Fqun10%2Fqun10-AcousticModule';
            // let data={
            //     path:'/dfs_data/qun10/qun10-AcousticModule'
            // }
            // sendRequest(url, 'POST',data).then(res => {
            //     alert("请求成功\n" + JSON.stringify(res));
            // }).catch(res => {
            //     alert("请求失败\n");
            // })
            // let customConfig = {
            //     headers: {
            //         'Content-Type': 'application/json'
            //     }
            // }
            // let params=JSON.stringify({path:'/dfs_data/qun10/qun10-AcousticModule'})
            // axios.post('http://172.16.100.104:35000/minio_interface/data_warehousing_single',params,customConfig).then(res=>{
            //     console.log('请求成功')
            //     console.log(res.data)
            //     alert.log(JSON.stringify(res.data))
            // }).catch(err=>{
            //     console.log("请求失败")
            // })

            // let url='http://172.16.100.104:35000/minio_interface/data_warehousing_single'
            // let data={
            //     path:'/dfs_data/qun10/qun10-AcousticModule'
            // }
            // axios({
            //     url,
            //     data:qs.stringify(data),
            //     method:'post',
            //     headers:{
            //         'Content-Type':'application/json'
            //     }
            // }).then(res=>{
            //     console.log(res.data)
            //     JSON.stringify(res.data)
            // })

            // axios.post("http://172.16.100.104:35000/minio_interface/data_warehousing_single",
            // "path=/dfs_data/qun10/qun10-AcousticModule")
            // .then(res=>{
            //     alert(JSON.stringify(res.data))
            // }).catch(err=>{
            //     console.log(err)
            // })

            axios({
                url:'http://172.16.100.104:35000/minio_interface/data_warehousing_single',
                method:'post',
                data:{
                    path:'/dfs_data/qun10/qun10-AcousticModule'
                },
                transformRequest:[
                    function(data){
                        var onMyForm=new FormData();
                        onMyForm.append("path","/dfs_data/qun10/qun10-AcousticModule")
                        console.info(onMyForm)
                        return onMyForm
                    } 
                ],
                headers:{
                    'Content-Type':'application/x-www-form-urlencoded'
                }
            }).then(res=>{
                console.log(res.data)
                alert(JSON.stringify(res.data))
            }).catch(err=>{
                console.log(err)
            })

        },
        sendGet() {
            
            // let url='http://172.16.100.104:35000/es_interface/query_meta_data_in_es';

            // let data={
            //     tag:"仿真模拟",
            //     dataSource:"海洋水下导航"
            // }
            // sendRequest(url,'GET',data).then(res => {
            //     alert("请求成功\n" + JSON.stringify(res));
            // }).catch(res => {
            //     alert("请求失败\n");
            // })

            // sendRequest(test, 'post', {},{'content-type' : 'application/x-www-form-urlencoded'}).then(res => {
            //     alert("请求成功\n" + JSON.stringify(res));
            // }).catch(res => {
            //     alert("请求失败\n");
            // })
            
            this.api_get("/es_interface/query_meta_data_in_es")
        },
        api_get(path){
            let url = BASE_URL+path+"?tag=仿真数据&data_source=海洋水下导航";
            axios.get(
                url
            ).then((res)=>{
                console.log(res.data)
                alert("请求成功！\n"+
                JSON.stringify(res.data))
            }).catch(err=>{
                console.error("api_get "+url+" failed with parameters "+"with error info "+err)
            })
        },
    }
}

</script>