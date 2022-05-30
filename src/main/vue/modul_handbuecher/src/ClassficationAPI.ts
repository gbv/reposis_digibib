import {Classification, ClassificationCategory} from "@/Model";

export class ClassficationAPI {

    public static getLabel(classification: Classification | ClassificationCategory, id: string, lang: string): string | null {
        if (classification.ID == id) {
            if ("label" in classification) {
                const labels = classification.label.filter(label => label.lang == lang);
                if (labels.length == 1) {
                    return labels[0].text;
                } else {
                    classification.label[0].text;
                }
            } else if ("labels" in classification) {
                const labels = classification.labels.filter(label => label.lang == lang);
                if (labels.length == 1) {
                    return labels[0].text;
                } else {
                    return classification.labels[0].text;
                }
            }
        } else {
            if (classification.categories != undefined) {
                for (let i = 0; i < classification.categories.length; i++) {
                    const category = classification.categories[i];
                    const label1 = this.getLabel(category, id, lang);
                    if (label1 != null) {
                        return label1;
                    }
                }
            }
        }
        return null;
    }
}
