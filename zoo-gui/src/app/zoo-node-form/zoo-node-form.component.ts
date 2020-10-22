import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ZooServiceService } from '../zoo-service.service';

@Component({
  selector: 'app-zoo-node-form',
  templateUrl: './zoo-node-form.component.html',
  styleUrls: ['./zoo-node-form.component.css']
})
export class ZooNodeFormComponent {
  addressForm = this.fb.group({
    path:  [null, Validators.required],
    value: [null, Validators.required],
    type: [null, Validators.required],
  });

  hasUnitNumber = false;

  valueObjType = [
    {name: 'None'},
    {name: 'java.lang.String'},
    {name: 'java.lang.Integer'},
    {name: 'java.lang.Double'},
    {name: 'java.lang.Float'},
  ];

  constructor(private fb: FormBuilder, private zooService: ZooServiceService) {
    zooService.selectedNode$.subscribe((node) => {
      this.addressForm.controls["path"].setValue(node.name);
      this.addressForm.controls["value"].setValue(node.value);
      let typeValue = (!!node.type)?node.type:"None";
      this.addressForm.controls["type"].setValue(typeValue);
    });
  }

  onSubmit() {
    let path = this.addressForm.controls["path"].value;
    let value = this.addressForm.controls["value"].value;
    let type = this.addressForm.controls["type"].value;
    if(type === "None")
      type = null;
    console.log(this.addressForm.controls["path"].value);
    console.log(this.addressForm.controls["value"].value);
    console.log(this.addressForm.controls["type"].value);

    let result = this.zooService.setNode(path,value,type );
    result.subscribe((result)=>{
      console.log(result);
    })
  }

}
