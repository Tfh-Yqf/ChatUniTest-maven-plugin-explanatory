<template>
    <div style="display: flex;flex-direction: column;">
        <div style="padding: 10px;display: flex;align-items: center;">
            <img :src="backPng" style="width: 20px;height: 20px;" @click="back" />
            <div style="width: 80%;text-align: center;color:white;font-size: 20px;font-weight: normal;">
                <a v-if="add">新建项目</a>
                <a v-else>编辑项目</a>
            </div>
        </div>
        <div style="display: flex;align-items: center;color:#9ea4aa;padding: 20px;">
            <div style="width: 100px;flex-shrink: 0;">项目标题</div>
            <div>
                <a-input v-model="MyData.name"></a-input>
            </div>
        </div>

        <div style="display: flex;align-items: center;color:#9ea4aa;padding: 20px;">
            <div style="width: 100px;flex-shrink: 0;">出发地</div>
            <div>
                <a-select style="width: 200px" v-model="MyData.start_port">
                    <a-select-option :value="item.name" v-for="(item, index) in city" :key="item.name">{{ item.name
                    }}</a-select-option>
                </a-select>
            </div>
        </div>

        <div style="display: flex;align-items: center;color:#9ea4aa;padding: 20px;">
            <div style="width: 100px;flex-shrink: 0;">目的地</div>
            <div>
                <a-select style="width: 200px" v-model.number="MyData.end_port">
                    <a-select-option :value="item.name" v-for="(item, index) in city" :key="item.name">{{ item.name
                    }}</a-select-option>
                </a-select>
            </div>
        </div>


        <div style="display: flex;align-items: center;color:#9ea4aa;padding: 20px;">
            <div style="width: 100px;flex-shrink: 0;">出发时间</div>
            <div style="display: flex;flex-direction: column;">
                <a-date-picker v-model="MyData.start_date" placeholder="开始日期" valueFormat="YYYY,MM,DD"
                    style="width: 200px;" />
                <a-time-picker v-model="MyData.start_time" placeholder="开始时间" valueFormat="HH,mm,ss"
                    style="width: 200px;margin-top: 10px;"></a-time-picker>
            </div>
        </div>


        <div style="display: flex;align-items: center;color:#9ea4aa;padding: 20px;">
            <div style="width: 100px;flex-shrink: 0;">到达时间</div>
            <div style="display: flex;flex-direction: column;">
                <a-date-picker v-model="MyData.end_date" placeholder="结束日期" valueFormat="YYYY,MM,DD"
                    style="width: 200px;" />
                <a-time-picker v-model="MyData.end_time" placeholder="结束时间" valueFormat="HH,mm,ss"
                    style="width: 200px;margin-top: 10px;"></a-time-picker>
            </div>
        </div>
        <div style="width: 100%;display: flex;flex-direction: row-reverse;">
            <div class="newProject" @click="draw_guiji()">+绘制轨迹</div>
            <span class="fs-5" style="color: white; margin-right: 55px">{{ error_message }}</span>
        </div>

        <div style="width: 100%;border: 1px solid grey;margin-top: 5px;"></div>

        <div class="list-group">
            <div class="list-group-item list-group-item-action list-group-item-dark" v-for="item in MyData.guiji"
                :key="item.id" style="margin-top: 8px;">
                {{ item.name }}
            </div>
        </div>

        <div class="guiji-message">
            <img class="go-back" :src="backPng" @click="go_back" />
            <div class="input-group input-group-lg" style="margin: 80px 0 20px 0;">
                <span class="input-group-text" id="addon-wrapping">路线标题</span>
                <input type="text" class="form-control" placeholder="未命名路径" aria-describedby="addon-wrapping"
                    v-model="tmp_guiji_name">
            </div>
            <div class="card text-bg-dark">
                <h5 class="card-header">颜色</h5>
                <div class="card-body">
                    <div class="row">
                        <button class="col color-div" style="background-color: #FF0000;"
                            @click="change_color([1, 0, 0, 1])">
                        </button>
                        <!-- new Earth.Color(1, 0, 0, 1) -->
                        <button class="col color-div" style="background-color: #00FF00;"
                            @click="change_color([0, 1, 0, 1])"></button>

                        <!-- new Earth.Color(0, 1, 0, 1) -->
                        <button class="col color-div" style="background-color: #0000FF;"
                            @click="change_color([0, 0, 1, 1])">
                        </button>
                        <!-- new Earth.Color(0, 0, 1, 1) -->
                    </div>

                    <div class="row">
                        <button class="col color-div" style="background-color: #FFFF00" @click="change_color([1, 1, 0, 1])">
                        </button>
                        <!-- new Earth.Color(1, 1, 0, 1), -->
                        <button class="col color-div" style="background-color: #FF00FF;"
                            @click="change_color([1, 0, 1, 1])">
                        </button>
                        <!-- new Earth.Color(1, 0, 1, 1), -->
                        <button class="col color-div" style="background-color: #00FFFF;"
                            @click="change_color([0, 1, 1, 1])">
                        </button>
                        <!-- new Earth.Color(0, 1, 1, 1), -->
                    </div>
                </div>
            </div>

            <div class="text-center" style="margin-top: 20px;">
                <button type="button" class="btn btn-outline-secondary add-trace-btn" @click="save_guiji">保存</button>
            </div>
        </div>


        <!-- 保存按钮 -->
        <div style="width: 100%;display: flex;justify-content: center;margin-top: 30px;" @click="save">
            <div class="newProject" @click="save">保存</div>
        </div>
    </div>
</template>

<script>
import $ from 'jquery'

export default {
    props: ["add", "data"],
    data() {
        return {
            colors: ["红色", "绿色", "蓝色"],
            backPng: require('@/assets/back.png'),
            MyData: {
                name: '未命名的项目',
                date: '',
                start_port: '',
                end_port: '',
                start_date: '',
                start_time: '',
                end_date: '',
                end_time: '',

                guiji: []
            },
            city: [
                {
                    name: '花莲港',
                    latitude: 121.3752,
                    longitude: 23.5910
                },
                {
                    name: '南沙港',
                    latitude: 113.3448,
                    longitude: 22.4839
                },
                {
                    name: '新海港',
                    latitude: 110.0922,
                    longitude: 20.0323
                }
            ],
            NewGuiji: {
                name: '轨迹路线1',
                color: '红色',
                data: []
            },
            tmp_guiji_data: [],
            tmp_guiji_name: "未命名路径",
            tmp_guiji_color: new Earth.Color(0, 1, 1, 1),
            error_message: null,
        }
    },
    mounted() {
        this.init();
    },
    methods: {
        DrawGuijiSuccess(e) {
            this.tmp_guiji_data = e;
            $(".guiji-message").css({
                "display": "inline-block"
            })
            this.error_message = "";
        },
        draw_guiji() {
            this.error_message = "输入Enter以结束路径绘制"
            this.$emit('drawGuiji', this.MyData.guiji.length + 1);
        },
        change_color(color) {
            this.tmp_guiji_color = new Earth.Color(color[0], color[1], color[2], color[3]);
            this.$emit('change_guiji_color', { color: color, id: this.MyData.guiji.length + 1 });
        },
        save_guiji() {
            this.MyData.guiji.push({
                id: this.MyData.guiji.length + 1,
                data: this.tmp_guiji_data,
                color: this.tmp_guiji_color,
                name: this.tmp_guiji_name,
            });
            this.tmp_guiji_data = [];
            this.tmp_guiji_name = "未命名路径";
            this.tmp_guiji_color = new Earth.Color(0, 1, 1, 1);
            this.go_back();
            console.log(this.MyData.guiji);
        },
        go_back() {
            $(".guiji-message").css({
                "display": "none"
            })
        },
        back() {
            this.$emit('back');
        },
        FindCity(e) {
            for (var i = 0; i < this.city.length; i++) {
                if (this.city[i].name == e)
                    return this.city[i];
            }
        },
        save() {
            console.log(this.MyData.start_port)
            if (this.MyData.start_port == '' || this.MyData.start_port == undefined || this.MyData.start_port == null) {
                this.$message.warn('出发地未选择');
                return;
            }
            if (this.MyData.end_port == '' || this.MyData.end_port == undefined || this.MyData.end_port == null) {
                this.$message.warn('目的地未选择');
                return;
            }
            this.MyData.start_port = this.FindCity(this.MyData.start_port);
            this.MyData.end_port = this.FindCity(this.MyData.end_port);


            if (this.add) {
                // 新建模式
                this.MyData.date = new Date();
                console.log(this.MyData);
                this.$emit('AddProject', this.MyData);
            }
            else
                // 编辑模式
                this.$emit('EditProject', this.MyData);
        },
        init() {
            // console.log(this.data)
            if (!this.add) {
                this.MyData = this.data;
                this.MyData.start_port = this.MyData.start_port.name;
                this.MyData.end_port = this.MyData.end_port.name;
            }
            // console.log(this.MyData);
        }
    }
}
</script>


<style scoped>
.newProject {
    background-color: #8ab4f8;
    width: 90px;
    height: 35px;
    color: #2d333e;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 5px;
    margin-right: 10px;
    font-weight: bold;
}

.newProject:hover {
    cursor: pointer;
}




.myrow {
    margin: 15px 0;
    color: white;
    font-size: large;
}

.label-center {
    line-height: 38px;
}

.my-btn {
    width: 50%;
    background-color: #8AB4F8;
}

.add-trace-btn {
    width: 140px;
    background-color: #8AB4F8;
    font-size: large;
}

.guiji-message {
    width: 400px;
    height: 100%;
    background-color: #202124;
    position: fixed;
    top: 0px;
    z-index: 9999;
    display: none;
}

.go-back {
    width: 20px;
    height: 20px;
    margin-top: 20px;
}

.color-div {
    width: 50px;
    height: 50px;
    margin: 5px 10px;
    border-radius: 10%;
    cursor: pointer;
}
</style>