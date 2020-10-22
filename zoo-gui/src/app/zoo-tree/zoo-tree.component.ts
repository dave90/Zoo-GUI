import { CollectionViewer, SelectionChange, DataSource } from '@angular/cdk/collections';
import { FlatTreeControl } from '@angular/cdk/tree';
import { Component, Injectable } from '@angular/core';
import { BehaviorSubject, merge, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ZooServiceService } from '../zoo-service.service';

/** Flat node with expandable and level information */
export class DynamicFlatNode {
  dataNode;
  constructor(public item: string, public level = 1, public expandable = false,
    public isLoading = false, public path: string = "") { }

  setDataNode(data){
    this.dataNode = data;
  };
}

/**
 * Database for dynamic data. When expanding a node in the tree, the data source will need to fetch
 * the descendants data from the database.
 */
/*
@Injectable({providedIn: 'root'})
export class DynamicDatabase {
  dataMap = new Map<string, string[]>([
    ['Fruits', ['Apple', 'Orange', 'Banana']],
    ['Vegetables', ['Tomato', 'Potato', 'Onion']],
    ['Apple', ['Fuji', 'Macintosh']],
    ['Onion', ['Yellow', 'White', 'Purple']]
  ]);

  //rootLevelNodes: string[] = ['Fruits', 'Vegetables'];
  rootLevelNodes: string[] = ['/'];

  initialData(): DynamicFlatNode[] {
    return this.rootLevelNodes.map(name => new DynamicFlatNode(name, 0, true));
  }

  getChildren(node: string): string[] | undefined {
    return this.dataMap.get(node);
  }

  isExpandable(node: string): boolean {
    return this.dataMap.has(node);
  }
}
*/

/**
 * File database, it can build a tree structured Json object from string.
 * Each node in Json object represents a file or a directory. For a file, it has filename and type.
 * For a directory, it has filename and children (a list of files or directories).
 * The input will be a json object string, and the output is a list of `FileNode` with nested
 * structure.
 */
export class DynamicDataSource implements DataSource<DynamicFlatNode> {

  dataChange = new BehaviorSubject<DynamicFlatNode[]>([]);

  get data(): DynamicFlatNode[] { return this.dataChange.value; }
  set data(value: DynamicFlatNode[]) {
    this._treeControl.dataNodes = value;
    this.dataChange.next(value);
  }

  constructor(private _treeControl: FlatTreeControl<DynamicFlatNode>,
    private _zooService: ZooServiceService) { }

  connect(collectionViewer: CollectionViewer): Observable<DynamicFlatNode[]> {
    this._treeControl.expansionModel.changed.subscribe(change => {
      if ((change as SelectionChange<DynamicFlatNode>).added ||
        (change as SelectionChange<DynamicFlatNode>).removed) {
        this.handleTreeControl(change as SelectionChange<DynamicFlatNode>);
      }
    });

    return merge(collectionViewer.viewChange, this.dataChange).pipe(map(() => this.data));
  }

  disconnect(collectionViewer: CollectionViewer): void { }

  /** Handle expand/collapse behaviors */
  handleTreeControl(change: SelectionChange<DynamicFlatNode>) {
    if (change.added) {
      change.added.forEach(node => this.toggleNode(node, true));
    }
    if (change.removed) {
      change.removed.slice().reverse().forEach(node => this.toggleNode(node, false));
    }
  }

  /**
   * Toggle the node, remove from display list
   */
  toggleNode(node: DynamicFlatNode, expand: boolean) {

    console.log("PATH " + node.path);
    const index = this.data.indexOf(node);

    if (!expand) {
      let count = 0;
      for (let i = index + 1; i < this.data.length
        && this.data[i].level > node.level; i++, count++) { }
      this.data.splice(index + 1, count);
      this.dataChange.next(this.data);
      return;
    }

    node.isLoading = true;
    this._zooService.getChildren(node.path).subscribe((n) => {
      console.log(n);
      if (!n["child"] || n["child"].length == 0){
        node.setDataNode(n);
        node.expandable = false;
        this.data[index] = Object.assign({}, node);
        node.isLoading = false;
        this.dataChange.next(this.data);
        return;
      }

      let nodes = n["child"].map(name =>
        new DynamicFlatNode(name, node.level + 1, true, false, node.path + "/" + name));

      this.data.splice(index + 1, 0, ...nodes);
      node.isLoading = false;
      this.dataChange.next(this.data);
    });


    /*
    const children = this._database.getChildren(node.item);
    const index = this.data.indexOf(node);
    if (!children || index < 0) { // If no children, or cannot find the node, no op
      return;
    }

    node.isLoading = true;

    setTimeout(() => {
      if (expand) {
        const nodes = children.map(name =>
          new DynamicFlatNode(name, node.level + 1, this._database.isExpandable(name)));
        this.data.splice(index + 1, 0, ...nodes);
      } else {
        let count = 0;
        for (let i = index + 1; i < this.data.length
          && this.data[i].level > node.level; i++, count++) {}
        this.data.splice(index + 1, count);
      }

      // notify the change
      this.dataChange.next(this.data);
      node.isLoading = false;
    }, 1000);
    */

  }
}

/**
 * @title Tree with dynamic data
 */
@Component({
  selector: 'app-zoo-tree',
  templateUrl: 'zoo-tree.component.html',
  styleUrls: ['zoo-tree.component.css']
})
export class ZooTreeComponent {
  constructor(private zooService: ZooServiceService) {
    this.treeControl = new FlatTreeControl<DynamicFlatNode>(this.getLevel, this.isExpandable);
    this.dataSource = new DynamicDataSource(this.treeControl, zooService);


    this.loadRoot();


    zooService.refreshRootSource$.subscribe((data)=>{
      this.loadRoot();
    })
    //this.dataSource.data = database.initialData();
  }

  loadRoot(){
    this.zooService.getRoot().subscribe((data) => {
      console.log(data);
      let mappedData = data["child"].map(node => new DynamicFlatNode(node, 0, true, false, "/" + node));
      this.dataSource.data = mappedData;
    });
  }

  treeControl: FlatTreeControl<DynamicFlatNode>;

  dataSource: DynamicDataSource;

  getLevel = (node: DynamicFlatNode) => node.level;

  isExpandable = (node: DynamicFlatNode) => node.expandable;

  hasChild = (_: number, _nodeData: DynamicFlatNode) => _nodeData.expandable;


  onClickLeaf(node){
    //this.zooService.selectNode(node.dataNode);
    console.log(node);
    this.zooService.getChildren(node.path).subscribe((n) => {
      this.zooService.selectNode(n);
    });

  }

  deleteNode(node){
    console.log("DELETE");
    console.log(node.path);
    this.zooService.deleteNode(node.path).subscribe((result) => {
      this.zooService.refreshRoot();
    },(result)=>{
      this.zooService.refreshRoot();
    })
  }
}
