import { Component } from '@angular/core';
import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'hello-world';
  data: string = '';

  constructor(private service: AppService) {
    service.getHelloWorld().subscribe(result => this.data = result.hello + ' ' + result.world);
  }
}
