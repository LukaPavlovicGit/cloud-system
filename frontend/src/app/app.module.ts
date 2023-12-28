import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { BrowserModule } from '@angular/platform-browser'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { HttpClientModule } from '@angular/common/http'
import { RouterOutlet } from "@angular/router"
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'

import { AppRoutingModule } from './app-routing.module'
import { AppComponent } from './components/app/app.component'
import { CreateUserComponent } from './components/create-user/create-user.component'
import { UpdateUserComponent } from './components/update-user/update-user.component'
import { DeleteUserComponent } from './components/delete-user/delete-user.component'
import { ReadUserComponent } from './components/read-user/read-user.component'
import { LoginUserComponent } from './components/login-user/login-user.component'
import { HomeComponent } from './components/home/home.component'

@NgModule({
  declarations: [
    AppComponent,
    CreateUserComponent,
    UpdateUserComponent,
    DeleteUserComponent,
    ReadUserComponent,
    LoginUserComponent,
    HomeComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterOutlet,
    AppRoutingModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
