import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable } from 'rxjs/index';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'my-app';
  data: string = '';

  constructor(private http: HttpClient) {
    this.getApiData().subscribe(result => this.data = result);
  }

  getApiData(): Observable<any> {
    return this.http.get('/api/hello-world', {responseType: 'text'});
  }
}
