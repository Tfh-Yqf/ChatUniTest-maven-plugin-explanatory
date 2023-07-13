
export const discount = (data,miny,maxy,yname,color,title)=>{
    // var minx = data.x[0],maxx = data.x[0];
    // var miny = data.y[0],maxy = data.y[0];
    // for(var i=0;i<data.x.length;i++){
    //     if(data.x[i]<minx)    minx = data.x[i];
    //     if(data.x[i]>maxx)    maxx = data.x[i];
    //     if(data.y[i]<miny)    miny = data.y[i];
    //     if(data.y[i]>maxy)    maxy = data.y[i];
    // }
    // minx = parseInt(minx);
    // maxx = parseInt(maxx);
    // console.log('minx'+minx);
    // console.log('maxx'+maxx);
    // miny = parseInt(miny);
    // maxy = parseInt(maxy);
    // console.log('miny'+miny);
    // console.log('maxy'+maxy);
    return  {
        xAxis: {
            type: 'category',
            data: data.x,
            name:"Time/s",
            show:true,
            axisLine:{
                show:true,
                symbol:['none', 'arrow']
            },
            nameGap:5
        },
        yAxis: {
            type: 'value',
            data:data.y,
            min:miny,
            max:maxy,
            name:yname,
            axisLine:{
                show:true,
                symbol:['none', 'arrow']
            }
        },
        series: [
            {
                data: data.y,
                type: 'line',
                lineStyle:{
                    color:color,
                    symbol:['none', 'arrow']
                }
            }
        ],
        title:{
            text: title,
            left:'center'
        }
    };
}

export const scatter_diagram = (data)=>{
    return {
        xAxis: {
            min:496,
            max:500
        },
        yAxis: {},
        series: [
            {
                symbolSize: 20,
                data: data,
                type: 'scatter'
            }
        ]
    };
}

export const third_chart = (data,title,data2)=>{
    var temp2 = [];
    for(var i=0;i<data2.east.length;i++){
        temp2.push([data2.east[i],data2.north[i],data2.up[i]/500]);
    }
    var symbolSize = 2.5;
    return {
        title: {
            text: title,
            left: 'center'
        },
        grid3D: [{}],
        xAxis3D: [{}],
        yAxis3D: [{}],
        zAxis3D: {
            min:-1,
            max:1
        },
        series: [
            {
                type: 'scatter3D',
                symbolSize: 10,
                data:temp2,
            },
            {
                type: 'scatter3D',
                symbolSize: symbolSize,
                data: data,
            },

        ]
    }
}


export const scatter = (data,title,data2)=>{
    var minx = data.x[0],maxx = data.x[0];
    var miny = data.y[0],maxy = data.y[0];
    for(var i=0;i<data.x.length;i++){
        if(data.x[i]<minx)    minx = data.x[i];
        if(data.x[i]>maxx)    maxx = data.x[i];
        if(data.y[i]<miny)    miny = data.y[i];
        if(data.y[i]>maxy)    maxy = data.y[i];
    }
    var temp = [];
    for(var i=0;i<data.x.length;i++){
        temp.push([data.x[i],data.y[i]]);
    }
    var temp2 = [];
    for(var i=0;i<data2.east.length;i++){
        temp2.push([data2.east[i],data2.north[i]]);
    }
    return {
        dataset: [
            {
                source: temp
            },
            {
                source: temp2
            }
        ],
        title: {
            text: title,
            left: 'center'
        },
        legend: {
            bottom: 22
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        xAxis: {
            splitLine: {
                lineStyle: {
                    type: 'dashed'
                }
            },
            min:minx-10,
            max:maxx+10
        },
        yAxis: {
            splitLine: {
                lineStyle: {
                    type: 'dashed'
                }
            }
        },
        series: [
            {
                name: 'scatter',
                type: 'scatter'
            },
            {
                name: 'TSPD',
                type: 'scatter',
                data:temp2,
                itemStyle:{
                    color:'#7b1823'
                }
            },
        ]
    };
}