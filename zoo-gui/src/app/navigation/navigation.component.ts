import { Component, ElementRef, ViewChild, Inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { ZooServiceService } from "../zoo-service.service";
import { MatDialogConfig, MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';


@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent {


  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  @ViewChild('inputfile') inputFile;

  constructor(public dialog: MatDialog, private zooService: ZooServiceService, private breakpointObserver: BreakpointObserver) { }

  openOptions() {
    let dialogRef = this.dialog.open(DialogOptionNavigator, {
      data: {
        file: null,
        zooService: this.zooService
      },
      height: "300px"
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  fileChanged(e) {
    let file = e.target.files[0];
    console.log(file);
    let dialogRef = this.dialog.open(DialogUploadNavigator, {
      data: {
        file: file,
        zooService: this.zooService
      },
      height: "300px",

    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
    //this.uploadDocument(file);
  }

  uploadDocument(file) {
    let fileReader = new FileReader();
    fileReader.onload = (e) => {
      console.log(fileReader.result);
      let xml = fileReader.result;
      this.zooService.sendXML(xml).subscribe((result) => {
        console.log(result);
      });
    }
    fileReader.readAsText(file);
    //this.inputFile.nativeElement.value = '';
  }



}


export interface DialogData {
  file,
  zooService
}
@Component({
  selector: 'dialog-content-file-upload-dialog',
  template: `<h2 mat-dialog-title>Loading XML</h2>
            <mat-dialog-content>
              <mat-spinner style="overflow: hidden;" *ngIf="loading"></mat-spinner>
              <mat-spinner [value]="100" [mode]="'determinate'" style="overflow: hidden;" *ngIf="!loading"></mat-spinner>
              <span *ngIf="!loading">Loading complete</span>
            </mat-dialog-content>
            <mat-dialog-actions align="end">
              <button mat-button mat-dialog-close>Cancel</button>
            </mat-dialog-actions>
            `,
})
export class DialogUploadNavigator {

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
    if (data.file != null)
      this.uploadDocument(data.file, data.zooService);
  }

  loading = true;

  uploadDocument(file, zooService) {
    let fileReader = new FileReader();
    fileReader.onload = (e) => {
      console.log(fileReader.result);
      let xml = fileReader.result;
      zooService.sendXML(xml).subscribe((result) => {
        console.log(result);
        this.loading = false;
        //this.inputFile.nativeElement.value = '';
      }, (error) => {
        this.loading = false;
      });
    }
    fileReader.readAsText(file);
  }
}


@Component({
  selector: 'dialog-content-file-upload-dialog',
  template: `<h2 mat-dialog-title>Options</h2>
            <mat-dialog-content>
            <p>
              <mat-form-field appearance="legacy">
                <mat-label>Back End</mat-label>
                <input  [(ngModel)]="zooService.be" matInput placeholder="Placeholder">
              </mat-form-field>
            </p>
            <p>
              <mat-form-field appearance="standard">
                <mat-label>Zookeeper Server</mat-label>
                <input [(ngModel)]="zooService.zoo" matInput placeholder="Placeholder">
              </mat-form-field>
            </p>            
            </mat-dialog-content>
              <mat-dialog-actions align="end">
                <button mat-button mat-dialog-close>Cancel</button>
              </mat-dialog-actions>
            `,
})
export class DialogOptionNavigator {

  zooService;


  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.zooService = data.zooService;
  }

}


