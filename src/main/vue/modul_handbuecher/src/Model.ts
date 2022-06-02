import {reactive} from 'vue'

export const modelGlobal: Model = reactive({
    currentLang: "de",
    baseURL: (<any>window).webApplicationBaseURL,
    crumbs: [],
    i18n: reactive({}),
    classifications: {},
    searchComplete: false,
    search: undefined,
    faculty: undefined,
    subject: undefined,
    discipline: undefined,
    start: 0,
    subjects: [],
    faculties: [],
    disciplines: [],
    classLoaded: false,
    query: undefined,
    onlyValid: true
})

export interface Model {
    onlyValid: boolean;
    query: undefined|string;
    classLoaded: boolean;
    currentLang: string;
    baseURL: string;
    crumbs: Array<{ to: string, label: string|undefined }>;
    i18n: Record<string, string>;
    classifications:Record<string, Classification>;
    searchComplete: boolean;
    search: undefined|any;
    faculty: undefined|string;
    subject: undefined|string;
    discipline: undefined|string;
    start: undefined|number;
    subjects:Array<string>;
    faculties:Array<string>;
    disciplines:Array<string>;
}

export interface Classification {
    ID: string,
    label: Array<ClassificationLabel>,
    categories: Array<ClassificationCategory>
}

export interface ClassificationCategory {
    ID: string,
    labels: Array<ClassificationLabel>,
    categories: Array<ClassificationCategory>
}

export interface ClassificationLabel {
    lang: string,
    text: string,
    description: string
}