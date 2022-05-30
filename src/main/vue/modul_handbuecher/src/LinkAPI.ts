export class LinkAPI {
    public static getLink(faculty:string|undefined, discipline:string|undefined, subject:string|undefined, start:number|undefined, query:string|undefined) {
        let link = '/';
        if(faculty!=undefined) {
            link+=faculty;
            if(discipline!=undefined){
                link+="/"+discipline;
                if(subject!=undefined){
                    link+="/"+subject;
                }
            }
        }

        const queries = [];
        if(start!==undefined){
            queries.push("start=" + start);
        }

        if(query!==undefined && query.length>0){
            queries.push("query="+query);
        }

        if(queries.length>0){
            link+="?"+queries.join("&");
        }

        return link;
    }

}