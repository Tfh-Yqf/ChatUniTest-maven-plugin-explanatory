import {sound_api, Sound_GetFileDetail, Sound_initdata} from "@/api/sound";

export default  {
    methods: {
        load_position(e){
            this.have_load_position = true;

            this.$refs.map.add_guiji();
            this.$refs.map.add_point_tag();

            this.$message.success('绘制点位置成功');
            this.change_target_point();
        },
        change_file_data(e){
            var temp = {
                type:e,
                FileName:''
            }
            if(e=='position') temp.FileName = this.data.position;
            else temp.FileName = this.data.svp;

            var that = this;
            Sound_GetFileDetail(temp).then((res)=>{
                if(e=='position'){
                    this.mapdata.circle = res.data.circle;
                    this.mapdata.all_point_label = res.data.point;
                }else{
                    that.svp_chart = res.data;

                }
                that.$message.success('加载成功');
            });
        },
        init(){
            this.loading = true;
            Sound_initdata().then((res)=>{
                if(res.code==0){
                    this.loading = false;
                    this.init_data = res;
                    this.$message.success(res.message);
                }
            }).finally((res)=>{
                this.loading = false;

            })
        },
        change_mapdata_targetpoint(){
            this.tarpoint_have_change = false;
            this.$refs.map.change_target_point();
            this.$message.success('修改目标位置成功');
        },
        change_tarpoint_have_change(){
            this.tarpoint_have_change = true;
        },
        change_time_kuadu(e){
            this.time.range_now = '';
            this.time.time_kuadu = e;
            this.time.range = [];
            switch (e){
                case 0:
                    for(var i=1990;i<2030;i++){
                        this.time.range.push({name:i+'年',value:i});
                    }
                    break;
                case 1:
                    for(var i=1;i<=12;i++){
                        this.time.range.push({name:i+'月',value:i});
                    }
                    break;
                case 2:
                    for(var i=1;i<=31;i++){
                        this.time.range.push({name:i+'日',value:i});
                    }
                    break;
            }
        },
        change_map_have_change(){
            this.map_have_change = true;
        },
        change_mapdata(){
            this.map_have_change = false;
            this.$refs.map.change_plane();
            this.$message.success('修改空间范围成功');
        },
        change_past_data(){
            this.time.past_data = !this.time.past_data;
        },
        excute(e){
            console.log(this.data);
            this.data.start_date = this.time.start_date+","+this.time.start_time;
            this.data.end_date = this.time.end_date+","+this.time.end_time ;
            this.loading = true;



            sound_api(this.data).then((res)=>{

                console.log(res);
                if(res.code!=0||res.code==undefined){
                    this.$message.error(res.message||'错误！');
                }else{

                    this.$message.success('执行成功！');
                    this.$store.commit('setSound',res.data);
                    this.$router.push('/sound/result');
                }
                this.loading=false;
            }).finally(()=>{
                this.loading=false;
            })
        },
        file_upload_templature(e){
            this.data.file.templature = e.file.response;
        },
        file_upload_config(e){
            this.data.file.config = e.file.response;
        },
        file_upload_sonar(e){
            this.data.file.sonar   = e.file.response;
        },
        change_suanfa(e){

            if(e=='Algorithm02'||e=='Algorithm01'){
                this.show_sonar  = false
            }
            else{
                this.show_sonar = true
            }

        }

    },
}