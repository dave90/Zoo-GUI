import { Component } from '@angular/core';
import { map } from 'rxjs/operators';
import { Breakpoints, BreakpointObserver } from '@angular/cdk/layout';
import { ZooServiceService} from "../zoo-service.service";

@Component({
  selector: 'app-my-dashboard',
  templateUrl: './my-dashboard.component.html',
  styleUrls: ['./my-dashboard.component.css']
})
export class MyDashboardComponent {
  constructor(public zooService: ZooServiceService,private breakpointObserver: BreakpointObserver) {}

  changeRequestObj(){
    this.zooService.changeRequestObj();
  }

}
