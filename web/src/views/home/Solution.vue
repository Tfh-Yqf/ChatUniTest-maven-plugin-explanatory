<template>
    <div>
        <div v-if="!show_edit" style="background-color: #202124;display: flex;flex-direction: column;">
            <div style="display: flex;margin-top: 10px;justify-content: space-between;">
                <div style="color: white;font-size: 20px;margin-left: 10px;">方案</div>
                <div class="newproject" @click="Add_project">新项目</div>
            </div>
            <div
                style="background-color: #2a2b2e;display: flex;align-items: center;justify-content: space-between;height: 42px;margin-top: 10px;">
                <div style="color: #9ea4aa;margin-left: 15px;font-weight: bold;">最近</div>
                <div style="display: flex;height: 100%;align-items: center;margin-right: 20px;">
                    <div :class="{ 'circle': circle_more }" style="margin-left: 10px;width: 30px;height: 30px;">
                        <img :src="more" style="width: 30px;height: 30px;" @mouseover="change_circle"
                            @mouseleave="change_circle" />
                    </div>
                    <div :class="{ 'circle': circle_up }"
                        style="margin-left: 10px;width: 30px;height: 30px;flex-shrink: 0;display: flex;align-items: center;justify-content: center;">
                        <img :src="up" style="width: 18px;height: 18px;" @mouseover="change_up" @mouseleave="change_up" />
                    </div>
                </div>
            </div>
            <div v-if="show_fangan == true">
                <div @click="start_show(index)" :class="{ 'over': xiangmu_over == index }" v-for="(item, index) in xiangmu"
                    :key="index" style="display: flex;padding: 10px;justify-content: space-between;"
                    @mouseover="change_over(index)" @mouseleave="remove_over">
                    <div class="list-item">
                        <div style="font-size: 17px;color: #e2e1e7;">{{ item.name }}</div>
                        <div style="display: flex;margin-top: 5px;align-items: center;">
                            <img :src="loc" style="width: 25px;height: 25px;" />
                            <div style="margin-left: 5px;color:#9aa0a6;font-size: 13px">港口:{{ item.start_port.name }}({{
                                item.start_port.latitude }},{{ item.start_port.longitude }})-{{ item.end_port.name
    }}({{ item.end_port.latitude }},{{ item.end_port.longitude }})</div>
                            <!-- <div style="margin-left: 10px;color:#9aa0a6">{{ item.date }}</div> -->
                        </div>
                    </div>

                    <div style="display: flex;align-items: center;" v-if="xiangmu_over == index">
                        <img @click="Edit_project(item)" :src="edit" style="width: 20px;height: 20px;">
                        <img @click="Del_project(e)" :src="del" style="width: 21px;height: 21px;margin-left: 10px;">
                    </div>
                </div>
            </div>
        </div>


        <Edit ref="edit" :add="add" :data="edit_data" v-else @back="change_show_edit" @AddProject="Add_xiangmu"
            @EditProject="Edit_xiangmu" @drawGuiji="drawGuiji" @change_guiji_color="change_guiji_color"></Edit>
    </div>
</template>

<script>
export default {
    components: {
        Edit: () => import('@/views/home/Edit.vue')
    },
    data() {
        return {
            more: require('@/assets/more.png'),
            up: require('@/assets/up.png'),
            loc: require('@/assets/loc.png'),
            del: require('@/assets/delete.png'),
            edit: require('@/assets/edit.png'),
            xiangmu: [
                {
                    name: '未命名的项目',
                    date: '2022-02-11',
                    end_port: {
                        name: '花莲港',
                        latitude: 121.3752,
                        longitude: 23.5910
                    },
                    start_port: {
                        name: '南沙港',
                        latitude: 113.3448,
                        longitude: 22.4839
                    },
                    start_date: '',
                    start_time: '',
                    end_date: '',
                    end_time: '',
                    guiji: [
                        {
                            data: [
                                [113.3448, 22.4839, 100],
                                [117.65020, 20.70812, 2],
                                [121.7969082, 21.554577, 100],
                                [121.3752, 23.5910, 100]
                            ],
                            color: new Earth.Color(1, 0, 0, 1),
                            name: '时间最短'

                        }, {
                            data: [
                                [113.3448, 22.4839],
                                [118.70975, 23.38190108],
                                [121.09378, 25.64084159],
                                [122.70877350, 25.154541],
                                [121.3752, 23.5910]
                            ],
                            color: new Earth.Color(1, 1, 0, 1),
                            name: '风险最低'
                        }, {
                            data: [
                                [113.3448, 22.4839],
                                [123.170443, 20.374814],
                                [121.3752, 23.5910]
                            ],
                            color: new Earth.Color(0, 1, 1, 1),
                            name: '导航效能最好'
                        }
                    ]
                },
                {
                    name: '未命名的项目1',
                    date: '2022-02-12',
                    start_port: {
                        name: '新海港',
                        latitude: 110.0922,
                        longitude: 20.0323
                    },
                    end_port: {
                        name: '花莲港',
                        latitude: 121.3752,
                        longitude: 23.5910
                    },
                    start_date: '',
                    start_time: '',
                    end_date: '',
                    end_time: '',
                    guiji: [
                        {
                            data: [
                                [113.3448, 22.4839, 100],
                                [117.65020, 20.70812, 2],
                                [121.7969082, 21.554577, 100],
                                [121.3752, 23.5910, 100]
                            ],
                            color: new Earth.Color(1, 0, 0, 0.5),
                            name: '时间最短'

                        }, {
                            data: [
                                [113.3448, 22.4839],
                                [118.70975, 23.38190108],
                                [121.09378, 25.64084159],
                                [122.70877350, 25.154541],
                                [121.3752, 23.5910]
                            ],
                            color: new Earth.Color(1, 1, 0, 0.5),
                            name: '风险最低'
                        }, {
                            data: [
                                [113.3448, 22.4839],
                                [123.17044342968748, 21.37481475233583],
                                [121.3752, 23.5910]
                            ],
                            color: new Earth.Color(0, 1, 1, 0.5),
                            name: '导航效能最好'
                        }
                    ]
                }
            ],
            circle_more: false,
            circle_up: false,
            xiangmu_over: -1,
            show_fangan: true,
            show_edit: false,
            add: false,
            edit_data: {}
        }
    },
    methods: {
        DrawGuijiSuccess(e) {
            this.$refs.edit.DrawGuijiSuccess(e);
        },
        drawGuiji(e) {
            this.$emit('drawGuiji', e);
        },
        change_guiji_color(params) {
            this.$emit('change_guiji_color', params);
        },
        Edit_xiangmu(e) {
            // 根据date查找
            for (var i = 0; i < this.xiangmu.length; i++) {
                if (this.xiangmu[i].date == e.date) {
                    this.xiangmu[i] = e;
                }
            }
            this.change_show_edit();
        },
        Add_xiangmu(e) {
            this.xiangmu.push(e);
            this.change_show_edit();
        },
        Del_project(e) {
            this.xiangmu.splice(e, 1);
        },
        Add_project() {
            this.add = true;
            this.change_show_edit();
        },
        Edit_project(e) {
            // 跳转到Edit页面
            this.add = false;
            this.edit_data = e;
            this.change_show_edit();
        },
        change_show_edit() {
            this.show_edit = !this.show_edit;
        },
        change_circle() {
            this.circle_more = !this.circle_more;
        },
        change_up() {
            this.circle_up = !this.circle_up;
        },
        change_over(e) {
            this.xiangmu_over = e;
        },
        remove_over() {
            this.xiangmu_over = -1;
        },
        start_show(e) {
            this.$emit('ShowGuiJi', this.xiangmu[e].guiji);
            this.$emit('hide_extent_box');
        }
    }
}
</script>

<style scoped>
.newproject {
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

.newproject:hover {
    cursor: pointer;
}

.circle {
    background-color: #313235;
    border-radius: 200px;
}

.over {
    background-color: #313235;
    border-radius: 5px;
}

.list-item {
    display: flex;
    flex-direction: column;
    margin-left: 15px;
    cursor: pointer;
}
</style>