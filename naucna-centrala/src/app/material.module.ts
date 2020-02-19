import { MatButtonModule, MatToolbarModule, MatIconModule, MatCardModule, MatFormFieldModule, MatInputModule } from '@angular/material';
import {  MatCheckboxModule, MatSelectModule } from '@angular/material';
import { NgModule } from '@angular/core';

@NgModule({
  declarations: [],
  imports: [ MatButtonModule,
             MatToolbarModule,
             MatIconModule,
             MatCardModule,
             MatCheckboxModule,
             MatFormFieldModule,
             MatSelectModule,
             MatInputModule],
  exports: [ MatButtonModule,
             MatToolbarModule,
             MatIconModule,
             MatCardModule,
             MatCheckboxModule,
             MatFormFieldModule,
             MatSelectModule,
             MatInputModule]
})
export class MaterialModule { }
