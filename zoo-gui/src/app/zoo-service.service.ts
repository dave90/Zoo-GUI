import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ZooServiceService {

  private DEFAULT_BE = "http://127.0.0.1:8081";
  private DEFAULT_ZOO = "127.0.0.1:2181"

  requestObject = false;

  zoo = this.DEFAULT_ZOO;
  be = this.DEFAULT_BE;

  private selectedNodeSource = new Subject<any>();
  selectedNode$ = this.selectedNodeSource.asObservable();

  private refreshRootSource = new Subject<any>();
  refreshRootSource$ = this.refreshRootSource.asObservable();


  constructor(private http: HttpClient) {
    //this.client = createClient(this.DEFAULT_ZOO);
  }

  getRoot() {

    return this.http.get(this.be + "/get_node?zoo=" + this.zoo + "&path=/");

  }

  getChildren(path) {
    if (!this.requestObject)
      return this.http.get(this.be + "/get_node?zoo=" + this.zoo + "&path=" + path);
    else
      return this.http.get(this.be + "/get_node?zoo=" + this.zoo + "&path=" + path+"&encode=0");
  }


  selectNode(node) {
    this.selectedNodeSource.next(node);
  }

  setNode(path, value, type) {
    console.log("CALL");

    if (!!type) {
      console.log(this.be + "/set_node?zoo=" + this.zoo + "&path=" + path + "&value=" + value + "&type=" + type);
      return this.http.get(this.be + "/set_node?zoo=" + this.zoo + "&path=" + path + "&value=" + value + "&type=" + type);
    } else {
      console.log(this.be + "/set_node?zoo=" + this.zoo + "&path=" + path + "&value=" + value);
      return this.http.get(this.be + "/set_node?zoo=" + this.zoo + "&path=" + path + "&value=" + value);

    }

  }

  changeRequestObj() {
    this.requestObject = !this.requestObject;
  }

  getRequestObj(){
    return this.requestObject;
  }

  sendXML(xml){
    let headers = new HttpHeaders({'Content-Type': 'text/plain'});
    //headers = headers.set('Access-Control-Allow-Origin', '*');


    if (!this.requestObject)
      return this.http.post(this.be + "/load_xml?zoo=" + this.zoo,xml,{headers:headers});
    else
      return this.http.post(this.be + "/load_xml?zoo=" + this.zoo+"&encode=0",xml,{headers:headers});
  }

  refreshRoot(){
    this.refreshRootSource.next("");
  }

  deleteNode(path) {
      return this.http.delete(this.be + "/delete_node?zoo=" + this.zoo + "&path=" + path);
  }

}
