
export const sound_result_chart = (data) => {
    return {
        dataset: [
            {
                id: 'dataset_raw',
                source: _rawData
            },
            {
                id: 'dataset_since_1950_of_germany',
                fromDatasetId: 'dataset_raw',
                transform: {
                    type: 'filter',
                    config: {
                        and: [
                            { dimension: 'Year', gte: 1950 },
                            { dimension: 'Country', '=': 'Germany' }
                        ]
                    }
                }
            },
            {
                id: 'dataset_since_1950_of_france',
                fromDatasetId: 'dataset_raw',
                transform: {
                    type: 'filter',
                    config: {
                        and: [
                            { dimension: 'Year', gte: 1950 },
                            { dimension: 'Country', '=': 'France' }
                        ]
                    }
                }
            }
        ],
        title: {
            text: 'Income of Germany and France since 1950'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            nameLocation: 'middle'
        },
        yAxis: {
            name: 'Income'
        },
        series: [
            {
                type: 'line',
                datasetId: 'dataset_since_1950_of_germany',
                showSymbol: false,
                encode: {
                    x: 'Year',
                    y: 'Income',
                    itemName: 'Year',
                    tooltip: ['Income']
                }
            },
            {
                type: 'line',
                datasetId: 'dataset_since_1950_of_france',
                showSymbol: false,
                encode: {
                    x: 'Year',
                    y: 'Income',
                    itemName: 'Year',
                    tooltip: ['Income']
                }
            }
        ]
    };
}

export const sound_2d_01 = (data) =>{
    var minn = 1000000,maxx = -100000;
    
    // for(var i=0;i<data.test.length;i++){
    //     if(data.test[i][1]<minn)    minn = data.test[i][1];
    //     if(data.tar[i][1]<minn)    minn = data.tar[i][1];
    //
    //     if(data.test[i][1]>maxx)    maxx = data.test[i][1];
    //     if(data.tar[i][1]<maxx)    maxx = data.tar[i][1];
    // }

    for(var i=0;i<data.test.length;i++){
        if(data.test[i][0]<minn)    minn = data.test[i][0];

        if(data.test[i][0]>maxx)    maxx = data.test[i][0];

    }
    minn = parseInt(minn);
    maxx = parseInt(maxx)+10;
    return {
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
            right:'5%'
        },

        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'value',
            // nameLocation: 'middle',
            name:'SSP M/S',
            min:minn,
            max:maxx,
            maxInterval:parseInt((maxx-minn)/10),
        },
        yAxis: {
            name: 'Depth M',
            type:'value',

        },
        legend: {
            type:'plain',
            x:'left',
            textStyle: {
                color: "#fff",
            }
        },
        series: [
            {
                type: 'line',
                data:data.test,
                showSymbol: false,
                encode: {
                    x: 'test',
                    y: 'depth',
                    itemName: 'Line 1',
                    tooltip: ['depth','test']
                },
                name:'构建值'
            },
            {
                type: 'line',
                data:data.tar,
                showSymbol: false,
                encode: {
                    x: 'tar',
                    y: 'depth',
                    itemName: 'Line 2',
                    tooltip: ['depth']
                },
                name:'真值'
            }
        ]
    };
}

export const sound_2d_svp = (data) =>{
    var minn = 1000000,maxx = -100000;
    var min_depth = 1000000,max_depth = -100000;
    // for(var i=0;i<data.test.length;i++){
    //     if(data.test[i][1]<minn)    minn = data.test[i][1];
    //     if(data.tar[i][1]<minn)    minn = data.tar[i][1];
    //
    //     if(data.test[i][1]>maxx)    maxx = data.test[i][1];
    //     if(data.tar[i][1]<maxx)    maxx = data.tar[i][1];
    // }

    for(var i=0;i<data[0].length;i++){
        if(data[0][i][0]<minn)    minn = data[0][i][0];
        if(data[0][i][1]<min_depth)    min_depth = data[0][i][1];

        if(data[0][i][0]>maxx)    maxx = data[0][i][0];
        if(data[0][i][1]>max_depth)    max_depth = data[0][i][1];

    }
    minn = parseInt(minn)-5;
    maxx = parseInt(maxx)+10;

    min_depth = parseInt(min_depth)-5;
    max_depth = parseInt(max_depth)+5;
    return {
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
            right:'5%'
        },
        title: {
            text: '声速场剖面图',
            name:'depth'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'value',
            // nameLocation: 'middle',
            name:'SSP M/S',
            min:minn,
            max:maxx,
            maxInterval:parseInt((maxx-minn)/10)
        },
        yAxis: {
            name: 'Depth M',
            type:'value',
            min:min_depth,
            max:max_depth,
        },
        series: [
            {
                name:'Line 1',
                type: 'line',
                data:data[0],
                showSymbol: false,
                encode: {
                    x: 'test',
                    y: 'depth',
                    itemName: '1'
                }
            },
            {
                name:'Line 2',
                type: 'line',
                data:data[1],
                showSymbol: false,
                encode: {
                    x: 'tar',
                    y: 'depth',
                    itemName: '2'
                }
            },
            {
                name:'Line 3',
                type: 'line',
                data:data[2],
                showSymbol: false,
                encode: {
                    x: 'tar',
                    y: 'depth',
                    itemName: '2'
                }
            },
            {
                name:'Line 4',
                type: 'line',
                data:data[3],
                showSymbol: false,
                encode: {
                    x: 'tar',
                    y: 'depth',
                    itemName: '2'
                }
            },{
                name:'Line 5',
                type: 'line',
                data:data[4],
                showSymbol: false,
                encode: {
                    x: 'tar',
                    y: 'depth',
                    itemName: '2'
                }
            }
        ]
    };
}

export const sound_svm = (data) =>{
    var minn = 1000000,maxx = -100000;

    for(var i=0;i<data.test.length;i++){
        if(data.test[i][0]<minn)    minn = data.test[i][0];

        if(data.test[i][0]>maxx)    maxx = data.test[i][0];

    }
    minn = parseInt(minn);
    maxx = parseInt(maxx)+10;
    return {
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
            right:'5%'
        },

        title: {
            text: '声速场构建RMSE图',
            name:'depth'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'value',
            // nameLocation: 'middle',
            name:'SSP M/S',
            min:minn,
            max:maxx,
            maxInterval:parseInt((maxx-minn)/10),
        },
        yAxis: {
            name: 'Depth M',
            type:'value',

        },
        legend: {
            type:'plain'
        },
        series: [
            {
                type: 'line',
                data:data.test,
                showSymbol: false,
                encode: {
                    x: 'test',
                    y: 'depth',
                    itemName: 'Line 1',
                    tooltip: ['depth','test']
                },
                name:'构建值'
            }
        ]
    };
}
