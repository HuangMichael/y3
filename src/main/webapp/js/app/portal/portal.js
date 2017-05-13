$(document).ready(function () {
    App.setPage("inbox");  //Set current page
    App.init(); //Initialise plugins and elements
    Highcharts.setOptions({
        colors: ['#50B432', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4', '#058DC7'],
        exporting: {
            enabled: true
        },
    });

    loadChartData(addMonth(0));
    //默认加载当月数据


    $("#currentMonth").on("click", function () {
        var reportMonth = addMonth(0);
        loadChartData(reportMonth);
        $("#setupDate").val(reportMonth);
    });
    $("#lastMonth").on("click", function () {
        var reportMonth = addMonth(-1);
        loadChartData(reportMonth);
        $("#setupDate").val(reportMonth);
    })


    $("#displayBtn").on("click", function () {
        var reportMonth = $("#setupDate").val();
        reportMonth = (!reportMonth) ? addMonth(0) : reportMonth;
        loadChartData(reportMonth);

    })
});


/**
 *
 * @param reportMonth
 */
function loadChartData(reportMonth) {
    loadEqClassChart(reportMonth);
    loadReportFinishChart(reportMonth);
    loadLineChart(reportMonth, '已报修');
}

/**
 *加载设备分类统计
 * @param reportMonth
 */
function loadEqClassChart(reportMonth) {
    /**
     *
     * @param chart2Data
     * @returns {*}
     */
    var newData = [];

    function assembleData(chart2Data) {
        var sumOther = 0;
        chart2Data.forEach(function (e, i) {
            var obj = null;
            if (i < 5) {
                obj = {name: e[1], y: e[2]}
                newData.push(obj);
            } else {
                sumOther += e[2];
            }

        });
        if (sumOther) {
            newData.push({
                name: "其他分类",
                y: sumOther
            })
            return newData;
        }
    }

    //ajax 请求当月设备分类前5
    var chart2Data = [];
    $.getJSON("/portal/findTopEqClass/" + reportMonth, function (data) {
        chart2Data = assembleData(data);
    });
    var eqClassChartConfig = {
        chart: {
            type: 'pie'
        },
        title: {
            text: reportMonth + '报修按设备类型统计'
        },
        plotOptions: {
            series: {
                dataLabels: {
                    enabled: true,
                    format: '{point.name}: {point.y}'
                }
            }
        },
        exporting: {
            enabled: true
        },
        tooltip: {
            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>报修: <b>{point.y}</b>单/共<b>{point.total}</b>单<b>/占比:{point.percentage:.1f}%</b>'
        },
        series: [{
            name: '报修数量',
            colorByPoint: true,
            data: newData
        }]
    }
    $('#highCharts0').highcharts(eqClassChartConfig);


}


/**
 *加载设备分类统计
 * @param reportMonth
 */
function loadReportFinishChart(reportMonth) {
    var seriesOptions = [];
    var option0, option1;

    option0 = {
        "name": "报修数量",
        "data": get3MonthReportNum(reportMonth)
    };
    option1 = {
        "name": "完工数量",
        "data": get3MonthFinishNum(reportMonth)
    };
    seriesOptions.push(option0);
    seriesOptions.push(option1);


    /**
     *
     * @param reportMonth
     * @returns {Array}
     */
    function get3MonthTitle() {
        var title = [];
        var date = new Date();
        title.push(getMonthAdd(date.getMonth(), -1) + "月");
        title.push(getMonthAdd(date.getMonth(), 0) + "月");
        title.push(getMonthAdd(date.getMonth(), 1) + "月");


        console.log("title month--------------" + title);
        return title;
    }


    function get3MonthReportNum(reportMonth) {
        $.ajaxSettings.async = false;
        var url = "/workOrderReport/sel3mRptNum";
        var reportNums = [];
        $.getJSON(url, function (data) {
            for (var x = 0; x < 3; x++) {
                if (data[x] && data[x]["reportNum"] && !isNaN(data[x]["reportNum"])) {
                    reportNums.push(data[x]["reportNum"]);
                } else {
                    reportNums.push(0);
                }
            }
        });

        console.log("reportNums-----------------------" + reportNums);
        return reportNums;
    }

    function get3MonthFinishNum() {
        $.ajaxSettings.async = false;
        var url = "/workOrderReport/sel3mFinishNum";
        var finishNums = [];
        $.getJSON(url, function (data) {
            for (var x = 0; x < 3; x++) {
                if (data[x] && data[x]["finishNum"] && !isNaN(data[x]["finishNum"])) {
                    finishNums.push(data[x]["finishNum"]);
                } else {
                    finishNums.push(0);
                }
            }
        });
        return finishNums;
    }

    var reportFinishChartConfig = {
        chart: {
            type: 'column'
        },

        exporting: {
            enabled: true
        },
        title: {
            text: '最近3个月报修完成情况统计'
        },
        /*    subtitle: {
         text: get3MonthTitle()
         },*/
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: get3MonthTitle()
        },
        yAxis: {
            min: 0,
            title: {
                text: '工单数量(单位:个)'
            }
        },
        series: seriesOptions
    }
    $('#highCharts1').highcharts(reportFinishChartConfig);


}


/**
 * 根据线路统计各状态的订单数量
 * @param reportMonth
 */
function loadLineChart(reportMonth, status) {
    var dataList0 = findLineStatusStat(reportMonth, '报修车');
    var dataList1 = findLineStatusStat(reportMonth, '已报修');
    var dataList2 = findLineStatusStat(reportMonth, '已完工');
    var dataList3 = findLineStatusStat(reportMonth, '已取消');
    var dataList4 = findLineStatusStat(reportMonth, '已暂停');
    // var drillDownSeries = getDrillDownSeries(reportMonth, status);
    $('#highCharts2').highcharts(
        {
            chart: {
                type: 'column'
            },
            title: {
                text: reportMonth + '各站区/段区维修单按照状态统计'
            },
            xAxis: {
                // type: 'category',

                categories: findLines("2017-05", "")
            },
            yAxis: {
                title: {
                    text: '维修工单数量'
                }
            },
            legend: {
                enabled: true
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: false,
                        format: '{point.y}'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b><br/>'
            },
            series: [{
                name: '报修车',
                colorByPoint: true,
                data: dataList0
            },
                {
                    name: '已报修',
                    colorByPoint: true,
                    data: dataList1
                },
                {
                    name: '已完工',
                    colorByPoint: true,
                    data: dataList2
                },
                {
                    name: '已取消',
                    colorByPoint: true,
                    data: dataList3
                },
                {
                    name: '已暂停',
                    colorByPoint: true,
                    data: dataList4
                }


            ]
        }
    );
}


/**
 *
 * @param value 当前月份值yi
 * @param step 前后偏移
 * @returns {number} 返回月份显示值
 */
function getMonthAdd(value, step) {
    return (value + step) % 12;

}


/**
 *
 * @param reportMonth 月份
 * @param status 工单状态 汉字描述
 * @returns {Array}
 */
function findLineStatusStat(reportMonth, status) {
    var lineData = [];
    $.ajaxSettings.async = false;
    var url = "/portal/findLineStatusStat";
    var data = {
        reportMonth: reportMonth,
        status: status
    };
    $.post(url, data, function (data) {
        lineData = data;
    });

    console.log("lineData-------------" + JSON.stringify(lineData));
    return lineData;
}


/**
 *
 * @param reportMonth 月份
 * @param status 工单状态 汉字描述
 * @returns {Array}
 */
function findLines(reportMonth, status) {
    var lines = [];
    $.ajaxSettings.async = false;
    var url = "/portal/findLineStatusStat";
    var data = {
        reportMonth: reportMonth,
        status: status
    };
    $.post(url, data, function (data) {
        for (var x in data) {
            lines[x] = data[x]["name"];
        }
    });

    return lines;
}

function getDrillDownSeries(reportMonth, status) {
    $.ajaxSettings.async = false;
    var drillDownSeries = [];
    // var url = "/portal/findStationStatusStat";
    // var params = {
    //     reportMonth: reportMonth,
    //     status: status
    // };
    // $.post(url, params, function (data) {
    //     for (var x in data) {
    //         if (data[x]) {
    //             console.log("--------------------------" + JSON.stringify(data[x]));
    //             var d = data[x]["data"];
    //             for (var i in d) {
    //                 var obj = [];
    //                 if (d[i]) {
    //                     obj.push(d[i]["str"]);
    //                     obj.push(d[i]["value"]);
    //                 }
    //             }
    //             drillDownSeries[x] = {id: data[x]["id"], name: data[x]["name"], data: obj};
    //         }
    //
    //     }
    // });
    // console.log("drillDownSeries-------------" + JSON.stringify(drillDownSeries));


    drillDownSeries =
        [{
            name: 'BJ02',
            id: 'BJ02',
            data: [
                [
                    'v11.0',
                    24.13
                ],
                [
                    'v8.0',
                    17.2
                ],
                [
                    'v9.0',
                    8.11
                ],
                [
                    'v10.0',
                    5.33
                ],
                [
                    'v6.0',
                    1.06
                ],
                [
                    'v7.0',
                    0.5
                ]
            ]
        }, {
            name: 'BJ08',
            id: 'BJ08',
            data: [
                [
                    'v40.0',
                    5
                ],
                [
                    'v41.0',
                    4.32
                ],
                [
                    'v42.0',
                    3.68
                ],
                [
                    'v39.0',
                    2.96
                ],
                [
                    'v36.0',
                    2.53
                ],
                [
                    'v43.0',
                    1.45
                ],
                [
                    'v31.0',
                    1.24
                ],
                [
                    'v35.0',
                    0.85
                ],
                [
                    'v38.0',
                    0.6
                ],
                [
                    'v32.0',
                    0.55
                ],
                [
                    'v37.0',
                    0.38
                ],
                [
                    'v33.0',
                    0.19
                ],
                [
                    'v34.0',
                    0.14
                ],
                [
                    'v30.0',
                    0.14
                ]
            ]
        }, {
            name: 'BJ010',
            id: 'BJ010',
            data: [
                [
                    'v35',
                    2.76
                ],
                [
                    'v36',
                    2.32
                ],
                [
                    'v37',
                    2.31
                ],
                [
                    'v34',
                    1.27
                ],
                [
                    'v38',
                    1.02
                ],
                [
                    'v31',
                    0.33
                ],
                [
                    'v33',
                    0.22
                ],
                [
                    'v32',
                    0.15
                ]
            ]
        }, {
            name: 'BJ13',
            id: 'BJ13',
            data: [
                [
                    'v8.0',
                    2.56
                ],
                [
                    'v7.1',
                    0.77
                ],
                [
                    'v5.1',
                    0.42
                ],
                [
                    'v5.0',
                    0.3
                ],
                [
                    'v6.1',
                    0.29
                ],
                [
                    'v7.0',
                    0.26
                ],
                [
                    'v6.2',
                    0.17
                ]
            ]
        }]


    return drillDownSeries;

}