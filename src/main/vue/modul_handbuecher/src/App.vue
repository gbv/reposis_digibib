<template>
  <div class="row">
    <div class="col-12">

      <h2>Modulhandbücher</h2>

    </div>
  </div>
  <div class="row">
    <div class="col-12 col-md-8">

      <search />

    </div>
    <div class="col-12 col-md-4">

      <h3>Ergebnisse einschränken</h3>

      <div class="card">
        <div class="card-header">
          <h4 class="card-title">Suchbegriff</h4>
        </div>
        <div class="card-body">
          <form class="form-inline">
            <input
              class="form-control mr-sm-2"
              type="search"
              placeholder="Schlagwort"
              v-on:change="queryChanged"
              v-on:keyup.prevent=""
              v-model="queryForm">
            <button
              class="btn btn-outline-success my-2 my-sm-0"
              type="submit"
              v-on:click.prevent="queryChanged">
              finden
            </button>
          </form>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h4 class="card-title">Kategorie</h4>
        </div>
        <div class="card-body">
          <ul
            v-if="model.classLoaded"
            class="mm-selected-categories">

            <li class="level-0">
              <router-link v-if="model.crumbs.length>0" to="/" >
                {{ model.i18n["digibib.module.crumb.root"] }}
              </router-link>
              <span v-else>
                {{ model.i18n["digibib.module.crumb.root"] }}
              </span>
            </li>

            <li
              v-for="(crumb, index) in model.crumbs"
              :key="crumb.to"
              :class="'level-' + parseInt(index + 1)">
              <router-link v-if="model.crumbs.length !== index + 1" :to="crumb.to">
                {{ crumb.label }}
              </router-link>
              <span v-else>
                {{ crumb.label }}
              </span>
            </li>

          </ul>
          <router-view/>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h4 class="card-title">Archiv</h4>
        </div>
        <div class="card-body">
          <div
            class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="checkbox"
              id="activeModuleCheckbox"
              v-model="model.onlyValid">
            <label
              class="form-check-label font-weight-normal"
              for="activeModuleCheckbox">
              {{model.i18n["digibib.module.active.modules"]}}
            </label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import Search from "@/components/Search.vue";
import {Classification, ClassificationCategory, Model, modelGlobal as globalModel} from "@/Model";
import {ClassficationAPI} from "@/ClassficationAPI";
import {LinkAPI} from "@/LinkAPI";
import {i18n} from "@/I18N";

@Options({
  components: {
    Search
  },
})
export default class App extends Vue {


  model: Model = globalModel;

  queryForm: string | undefined = undefined;

  queryChanged() {
    this.$router.push(LinkAPI.getLink(this.model.faculty,this.model.discipline,this.model.subject, this.model.start, this.queryForm));
  }

  async created() {

    const i18n_digibib_module_crumb_root = "digibib.module.crumb.root";
    const i18n_digibib_module_search = "digibib.module.search";
    const i18n_digibib_module_active_modules = "digibib.module.active.modules";
    const [
      discipline,
      genre,
      mir_institutes,
      i18ndmcr,
      i18ndms,
      i18nmdam
    ] = await Promise.all([
      this.loadClassification("discipline"),
      this.loadClassification("mir_genres"),
      this.loadClassification("mir_institutes"),
      i18n(this.model.baseURL, this.model.currentLang, i18n_digibib_module_crumb_root),
      i18n(this.model.baseURL, this.model.currentLang, i18n_digibib_module_search),
      i18n(this.model.baseURL, this.model.currentLang, i18n_digibib_module_active_modules)
    ]);

    this.model.i18n[i18n_digibib_module_crumb_root] = i18ndmcr;
    this.model.i18n[i18n_digibib_module_search] = i18ndms;
    this.model.i18n[i18n_digibib_module_active_modules] = i18nmdam;
    this.model.classLoaded = true;

    this.model.classifications["discipline"] = discipline;
    this.model.classifications["mir_genres"] = genre;
    this.model.classifications["mir_institutes"] = mir_institutes;

    this.$watch(
        () => this.$route.params,
        (toParams: any, previousParams: any) => {

          if (toParams.faculty != this.model.faculty ||
              toParams.subject != this.model.subject ||
              toParams.discipline != this.model.discipline) {
            this.model.faculty = toParams.faculty;
            this.model.subject = toParams.subject;
            this.model.discipline = toParams.discipline;
            this.model.start = this.$route.query.start == undefined || this.$route.query.start instanceof Array ? 0 : parseInt(this.$route.query.start, 10);

            this.refreshBreadcrumb();
            this.requestSolr();
          }
        }
    )

    this.$watch(() => this.model.onlyValid, (to: boolean, from: boolean) => {
      this.requestSolr();
    });

    this.$watch(() => this.$route.query.start,
        (p: any, prevP: any) => {
          if (prevP != p) {
            this.model.start = p == undefined ? 0 : parseInt(p, 10);
            this.requestSolr();
            console.log("page changed");
          }
        });

    this.$watch(()=> this.$route.query.query,
        (query: any, prevQ: any) => {
          if(query!=this.model.query) {
            this.model.query = query;
            this.queryForm = query;
            this.requestSolr();
          }
        }
    );

    if (this.$route.params.faculty instanceof Array || this.$route.params.subject instanceof Array || this.$route.params.discipline instanceof Array) {
      return
    }

    this.model.faculty = this.$route.params.faculty;
    this.model.subject = this.$route.params.subject;
    this.model.discipline = this.$route.params.discipline;
    this.model.start = this.$route.query.start == undefined || this.$route.query.start instanceof Array ? undefined : parseInt(this.$route.query.start, 10);
    this.model.query = this.queryForm= this.$route.query.query == undefined || this.$route.query.query instanceof Array ? undefined : this.$route.query.query;

    this.refreshBreadcrumb();
    await this.requestSolr();
  }

  private refreshBreadcrumb() {
    while (this.model.crumbs.length > 0) {
      this.model.crumbs.pop();
    }

    let pre = [];

    if (this.model.faculty != undefined) {
      pre.push({
        id: this.model.faculty,
        label: ClassficationAPI.getLabel(this.model.classifications["mir_institutes"], this.model.faculty, this.model.currentLang) || this.model.faculty
      });
    }

    if (this.model.discipline != undefined) {
      pre.push({
        id: this.model.discipline,
        label: ClassficationAPI.getLabel(this.model.classifications["discipline"], this.model.discipline, this.model.currentLang) || this.model.discipline
      });
    }

    if (this.model.subject != undefined) {
      pre.push({
        id: this.model.subject,
        label: this.model.subject
      });
    }

    let untilPath = ""
    for (let i = 0; i < pre.length; i++) {
      let curPre = pre[i];
      untilPath += "/" + curPre.id;
      this.model.crumbs.push(
          {to: untilPath, label: pre[i].label}
      );
    }
  }

  private async loadClassification(id: string): Promise<Classification> {
    let response = await fetch(`${this.model.baseURL}api/v1/classifications/${id}?format=json`);
    //let response = await fetch(`https://reposis-test.gbv.de/digibib/api/v1/classifications/${id}?format=json`);
    return await response.json();
  }

  private escape(value:string){
    if (value.match(/[ :/"]/) && !value.match(/[[{]\S+ TO \S+[\]}]/) && !value.match(/^["(].*[")]$/)) {
      return '"' + value.replace(/\\/g, '\\\\').replace(/"/g, '\\"') + '"';
    }
    return value;
  }

  private async requestSolr() {
    this.model.searchComplete = false;
    let {baseURL, faculty, subject, discipline, start, query} = this.model;
    let queryParam = `+objectType:"mods" +category:"mir_genres:module_manual"${this.model.onlyValid ? " +digibib.mods.validity_state:valid" : ""}`;
    let facetSubject = `facet.field=digibib.mods.subject.string`;
    let facetInstitute = `facet.field=digibib.mods.faculty`;
    let facetDiscipline = `facet.field=digibib.mods.discipline`;
    let facetSort = `facet.sort=index`;
    let url = `${baseURL}servlets/solr/select?fq=${encodeURIComponent(queryParam)}&${facetSubject}&${facetInstitute}&${facetDiscipline}&${facetSort}&wt=json&rows=20`;
    //let url = `https://reposis-test.gbv.de/digibib/servlets/solr/select?fq=${encodeURIComponent(queryParam)}&${facetSubject}&${facetInstitute}&${facetDiscipline}&${facetSort}&wt=json&rows=20`;


    if (faculty != undefined) {
      url += `&fq=digibib.mods.faculty:${this.escape(faculty)}`;
    }

    if (discipline != undefined) {
      url += `&fq=digibib.mods.discipline:${this.escape(discipline)}`;
    }

    if (subject != undefined) {
      url += `&fq=digibib.mods.subject.string:${this.escape(subject)}`;
    }

    if (start != undefined) {
      url += `&start=${encodeURIComponent(start)}`
    }

    if(query!=undefined && query.length>0){
      url+="&q=%2ballMeta:"+this.escape(query);
    } else {
      url+="&q=*:*";
    }

    let response = await fetch(url);

    this.model.search = await response.json();

    this.convertFacetToStringArray(this.model.subjects, this.model.search.facet_counts.facet_fields["digibib.mods.subject.string"]);
    this.convertFacetToStringArray(this.model.faculties, this.model.search.facet_counts.facet_fields["digibib.mods.faculty"]);
    this.convertFacetToStringArray(this.model.disciplines, this.model.search.facet_counts.facet_fields["digibib.mods.discipline"]);

    this.model.searchComplete = true;
  }


  private convertFacetToStringArray(target: Array<string>, facet: any) {
    while (target.length > 0) {
      target.pop()
    }
    for (let i = 0; i < facet.length; i += 2) {
      const str = facet[i];
      target.push(str);
    }
  }
}
</script>

<style scoped>

</style>
