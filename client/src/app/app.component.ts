import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable } from 'rxjs/index';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'hello-world';
  data: string = '';

  constructor(private http: HttpClient) {
    this.getApiData().subscribe(result => this.data = result.hello + ' ' + result.world);
  }

  getApiData(): Observable<HelloWorld> {
    return this.http.get<HelloWorld>('/api/hello-world');
  }
}

export interface HelloWorld {
  hello: string;
  world: string;
}
