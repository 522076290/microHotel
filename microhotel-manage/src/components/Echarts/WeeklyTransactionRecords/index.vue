<template>
    <div class="echarts-container">
      <!-- 图表容器 -->
      <div ref="chart" class="chart"></div>
    </div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  
  export default {
    name: 'EchartsBarChart',
    props: {
      // 你可以通过 props 传递数据和配置，以使组件更具复用性
      data: {
        type: Array,
        required: true,
        default: () => [
          { product: 'Matcha Latte', 2016: 85.8, 2017: 93.7 },
          { product: 'Milk Tea', 2016: 73.4, 2017: 55.1 },
          { product: 'Cheese Cocoa', 2016: 65.2, 2017: 82.5 },
          { product: 'Walnut Brownie', 2016: 53.9, 2017: 39.1 }
        ]
      },
      dimensions: {
        type: Array,
        default: () => ['product', 'income', 'outcome']
      }
    },
    data() {
      return {
        chartInstance: null,
      };
    },
    mounted() {
      this.initChart();
      // 监听窗口大小变化，自动调整图表大小
      window.addEventListener('resize', this.resizeChart);
    },
    beforeDestroy() {
      if (this.chartInstance) {
        this.chartInstance.dispose();
      }
      window.removeEventListener('resize', this.resizeChart);
    },
    methods: {
      initChart() {
        const chartDom = this.$refs.chart;
        this.chartInstance = echarts.init(chartDom);
        this.setChartOption();
      },
      setChartOption() {
        const option = {
          legend: {},
          tooltip: {},
          dataset: {
            dimensions: this.dimensions,
            source: this.data,
          },
          xAxis: { type: 'category' },
          yAxis: {},
          series: [
            { type: 'bar' },
            { type: 'bar' }
          ],
        };
        this.chartInstance.setOption(option);
      },
      resizeChart() {
        if (this.chartInstance) {
          this.chartInstance.resize();
        }
      }
    },
    watch: {
      // 监听数据变化，更新图表
      data: {
        handler() {
          if (this.chartInstance) {
            this.setChartOption();
          }
        },
        deep: true
      }
    }
  };
  </script>
  
  <style scoped>
  .echarts-container {
    width: 100%;
    height: 100%;
  }
  
  .chart {
    width: 100%;
    height: 400px; /* 你可以根据需要调整高度 */
  }
  </style>
  