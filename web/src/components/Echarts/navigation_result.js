



export const Coordinate_System_enu = (data) => {

    return {
        // Make gradient line here
        visualMap: [
            {
                show: false,
                type: 'continuous',
                seriesIndex: 0,
                min: 0,
                max: 400
            },
            {
                show: false,
                type: 'continuous',
                seriesIndex: 1,
                dimension: 0,
                min: 0,
                max: data.time.length - 1
            }
        ],
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                magicType: { type: ['line', 'bar'] },
                restore: {},
                saveAsImage: {}
            }
        },
        title: [
            {
                left: 'center',
                top: '0%',
                text: 'ENU 坐标系E轴',
                textStyle: {
                    fontSize: 16
                }
            },
            {
                top: '35%',
                left: 'center',
                text: 'ENU 坐标系N轴',
                textStyle: {
                    fontSize: 16
                }
            },
            {
                top: '69%',
                left: 'center',
                text: 'ENU 坐标系U轴',
                textStyle: {
                    fontSize: 16
                }
            }
        ],
        tooltip: {
            trigger: 'axis'
        },
        xAxis: [
            {
                data: data.time,
                gridIndex: 0
            },
            {
                data: data.time,
                gridIndex: 1
            },
            {
                data: data.time,
                gridIndex: 2
            },
        ],
        yAxis: [
            {},
            {
                gridIndex: 1
            },
            {
                gridIndex: 2
            },
        ],
        grid: [
            {
                top: '6%',
                height: '22%'
            },
            {
                top: '41%',
                height: '22%'
            },
            {
                top: '74%',
                height: '22%'
            },
        ],
        series: [
            {
                type: 'line',
                showSymbol: false,
                data: data.enu_e
            },
            {
                type: 'line',
                showSymbol: false,
                data: data.enu_n,
                xAxisIndex: 1,
                yAxisIndex: 1
            },
            {
                type: 'line',
                showSymbol: false,
                data: data.enu_u,
                xAxisIndex: 2,
                yAxisIndex: 2
            }
        ]
    };
}

export const Coordinate_System_att = (data) => {

    return {
        // Make gradient line here
        visualMap: [
            {
                show: false,
                type: 'continuous',
                seriesIndex: 0,
                min: 0,
                max: 400
            },
            {
                show: false,
                type: 'continuous',
                seriesIndex: 1,
                dimension: 0,
                min: 0,
                max: data.time.length - 1
            }
        ],
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                magicType: { type: ['line', 'bar'] },
                restore: {},
                saveAsImage: {}
            }
        },
        title: [
            {
                left: 'center',
                top: '0%',
                text: 'att 坐标系E轴',
                textStyle: {
                    fontSize: 16
                }
            },
            {
                top: '35%',
                left: 'center',
                text: 'att 坐标系N轴',
                textStyle: {
                    fontSize: 16
                }
            },
            {
                top: '69%',
                left: 'center',
                text: 'att 坐标系U轴',
                textStyle: {
                    fontSize: 16
                }
            }
        ],
        tooltip: {
            trigger: 'axis'
        },
        xAxis: [
            {
                data: data.time,
                gridIndex: 0
            },
            {
                data: data.time,
                gridIndex: 1
            },
            {
                data: data.time,
                gridIndex: 2
            },
        ],
        yAxis: [
            {},
            {
                gridIndex: 1
            },
            {
                gridIndex: 2
            },
        ],
        grid: [
            {
                top: '6%',
                height: '22%'
            },
            {
                top: '41%',
                height: '22%'
            },
            {
                top: '74%',
                height: '22%'
            },
        ],
        series: [
            {
                type: 'line',
                showSymbol: false,
                data: data.att_e
            },
            {
                type: 'line',
                showSymbol: false,
                data: data.att_n,
                xAxisIndex: 1,
                yAxisIndex: 1
            },
            {
                type: 'line',
                showSymbol: false,
                data: data.att_u,
                xAxisIndex: 2,
                yAxisIndex: 2
            }
        ]
    };
}



export const Coordinate_System_vel = (data) => {

    return {
        // Make gradient line here
        visualMap: [
            {
                show: false,
                type: 'continuous',
                seriesIndex: 0,
                min: 0,
                max: 400
            },
            {
                show: false,
                type: 'continuous',
                seriesIndex: 1,
                dimension: 0,
                min: 0,
                max: data.time.length - 1
            }
        ],
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                magicType: { type: ['line', 'bar'] },
                restore: {},
                saveAsImage: {}
            }
        },
        title: [
            {
                left: 'center',
                top: '0%',
                text: 'VEL 坐标系E轴',
                textStyle: {
                    fontSize: 16
                }
            },
            {
                top: '35%',
                left: 'center',
                text: 'VEL 坐标系N轴',
                textStyle: {
                    fontSize: 16
                }
            },
            {
                top: '69%',
                left: 'center',
                text: 'VEL 坐标系U轴',
                textStyle: {
                    fontSize: 16
                }
            }
        ],
        tooltip: {
            trigger: 'axis'
        },
        xAxis: [
            {
                data: data.time,
                gridIndex: 0
            },
            {
                data: data.time,
                gridIndex: 1
            },
            {
                data: data.time,
                gridIndex: 2
            },
        ],
        yAxis: [
            {},
            {
                gridIndex: 1
            },
            {
                gridIndex: 2
            },
        ],
        grid: [
            {
                top: '6%',
                height: '22%'
            },
            {
                top: '41%',
                height: '22%'
            },
            {
                top: '74%',
                height: '22%'
            },
        ],
        series: [
            {
                type: 'line',
                showSymbol: false,
                data: data.vel_e
            },
            {
                type: 'line',
                showSymbol: false,
                data: data.vel_n,
                xAxisIndex: 1,
                yAxisIndex: 1
            },
            {
                type: 'line',
                showSymbol: false,
                data: data.vel_u,
                xAxisIndex: 2,
                yAxisIndex: 2
            }
        ]
    };
}

export const Coordinate_System_denu = (data) => {

    return {
        title: {
            text: 'Denu 误差',
            left: '10%'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                magicType: { type: ['line', 'bar'] },
                restore: {},
                saveAsImage: {}
            },
            right: '5%'
        },
        legend: {
            data: ['Denu E', 'Denu N']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: data.time
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: 'Denu E',
                type: 'line',
                stack: 'Total',
                data: data.denu_e
            },
            {
                name: 'Denu N',
                type: 'line',
                stack: 'Total',
                data: data.denu_n
            }
        ]
    };
}

export const Coordinate_System_pdop = (data) => {

    return {
        title: {
            text: 'PDOP值',
            left: '10%'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                magicType: { type: ['line', 'bar'] },
                restore: {},
                saveAsImage: {}
            },
            right: '5%'
        },
        legend: {
            data: ['pdop']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: data.time
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: 'pdop',
                type: 'line',
                stack: 'Total',
                data: data.pdop
            }
        ]
    };
}




export const Coordinate_System_3DAnd2D = (data) => {
    var symbolSize = 2.5;
    var sizeValue = '57%';
    return {
        tooltip: {},
        grid3D: {
            top: '0%',
            height: '25%'
        },
        xAxis3D: {},
        yAxis3D: {},
        zAxis3D: {},
        grid: [
            {
                top: '75%',
                height: '25%'
            },
            {
                top: '25%',
                height: '25%'
            },
            {
                top: '50%',
                height: '25%'
            },
        ],
        xAxis: [
            {
                type: 'time',
                gridIndex: 0,
                name: 'Time',
                axisLabel: { rotate: 50, interval: 0 }
            },
            {
                type: 'time',
                gridIndex: 1,
                name: 'Time',
                boundaryGap: false,
                axisLabel: { rotate: 50, interval: 0 }
            },
            {
                type: 'time',
                gridIndex: 2,
                name: 'Time',
                axisLabel: { rotate: 50, interval: 0 }
            }
        ],
        yAxis: [
            { type: 'value', gridIndex: 0, name: 'E Axis' },
            { type: 'value', gridIndex: 1, name: 'N Axis' },
            { type: 'value', gridIndex: 2, name: 'U Axis' }
        ],
        dataset: {
            source: data
        },
        series: [
            {
                type: 'scatter3D',
                symbolSize: symbolSize,
                encode: {
                    x: 'e',
                    y: 'n',
                    z: 'u',
                }
            },
            {
                type: 'scatter',
                symbolSize: symbolSize,
                xAxisIndex: 0,
                yAxisIndex: 0,
                encode: {
                    x: 'time',
                    y: 'e',
                    tooltip: [0, 1, 2, 3]
                }
            },
            {
                type: 'scatter',
                symbolSize: symbolSize,
                xAxisIndex: 1,
                yAxisIndex: 1,
                encode: {
                    x: 'time',
                    y: 'n',
                    tooltip: [0, 1, 2, 3]
                }
            },
            {
                type: 'scatter',
                symbolSize: symbolSize,
                xAxisIndex: 2,
                yAxisIndex: 2,
                encode: {
                    x: 'time',
                    y: 'u',
                    tooltip: [0, 1, 2, 3]
                }
            }
        ]
    };
}

export const Coordinate_System_3D = (data) => {
    var symbolSize = 2.5;
    var sizeValue = '57%';
    return {
        tooltip: {},
        grid3D: {
            width: '100%'
        },
        xAxis3D: {},
        yAxis3D: {},
        zAxis3D: {},
        grid: [

        ],
        xAxis: [

        ],
        yAxis: [

        ],
        dataset: {
            source: data
        },
        series: [
            {
                type: 'scatter3D',
                symbolSize: symbolSize,
                encode: {
                    x: 'e',
                    y: 'n',
                    z: 'u',
                }
            },

        ]
    };
}

export const Coordinate_System_poserr = (data) => {

    return {
        title: {
            text: 'poserr',
            left: '10%'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                magicType: { type: ['line', 'bar'] },
                restore: {},
                saveAsImage: {}
            },
            right: '5%'
        },
        legend: {
            data: ['poserr laterr', 'poserr lonerr']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: data.time
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: 'poserr laterr',
                type: 'line',
                stack: 'Total',
                data: data.laterr
            },
            {
                name: 'poserr lonerr',
                type: 'line',
                stack: 'Total',
                data: data.lonerr
            }
        ]
    };
}
