<template>
  <div class="panel">
    <a-card title="Kalman滤波方差设置">
      <div style="display: flex;flex-direction: column;min-height: 250px;">
        <div v-for="(item, index) in items" :key="item.name">
          <mytable v-if="item.type != 'xiala'" :data="item"></mytable>
          <div v-else style="margin-top: 10px;">
            <a-collapse>
              <a-collapse-panel :header="item.name">
                <div v-for="(it, ind) in item.children" :key="it.name" style="display: flex;flex-direction: column">
                  <mytable v-if="it.type != 'xiala'" :data="it"></mytable>

                  <div style="width: 90%;margin-left: 5%;border-top: 1px solid #000000;margin-top: 10px;opacity: 0.1">
                  </div>
                </div>


              </a-collapse-panel>
            </a-collapse>
          </div>

          <div style="width: 90%;margin-left: 5%;border-top: 1px solid #000000;margin-top: 10px;opacity: 0.1"></div>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>

import moment from 'moment'

export default {
  name: 'NavgiationConfig',
  components: {
    mytable: () => import("@/components/MyTable/MyTable.vue")
  },
  props: ['items'],
  data() {
    return {
      moment,
      searchForm: { page: 1, is_return: false, ordering: '-date' },
      totalRows: 0,
      perPage: 5,

      tableHeight: window.innerHeight / 2 - 256,
    };
  },
  methods: {
    list(start_date, end_date) {
      this.loading = true;
      let form = { ...this.searchForm, ...{ start_date, end_date } };
      if (form.end_date) {
        form.end_date = moment(form.end_date).add(1, 'days').format('YYYY-MM-DD');
      }

      salesPaymentRecord(form).then(resp => {
        this.totalRows = resp.count;
        this.items = resp.results;
      }).finally(() => {
        this.loading = false;
      });
    },

  },
  mounted() {
  },
}
</script>

<style></style>