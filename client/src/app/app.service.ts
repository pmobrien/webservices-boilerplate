import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/index';

@Injectable()
export class AppService {

  constructor(private http: HttpClient) {}

  getHelloWorld(): Observable<HelloWorld> {
    return this.http.get<HelloWorld>('/api/hello-world');
  }
}

export interface HelloWorld {
  hello: string;
  world: string;
}
