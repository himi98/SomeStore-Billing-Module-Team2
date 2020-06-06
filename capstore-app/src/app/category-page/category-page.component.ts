import { Component, OnInit } from '@angular/core';
import { CapstoreService } from '../service/capstore.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Product } from '../Model/Product.model';

@Component({
  selector: 'app-category-page',
  templateUrl: './category-page.component.html',
  styleUrls: ['./category-page.component.css'],
})
export class CategoryPageComponent implements OnInit {
  category: string;
  filter: string = '';
  searchProduct: string = '';
  categoryProducts: Product[];
  allProducts: Product[];
  products: Product[];
  columnDisplay: number = 3;
  noShow: boolean = true;
  filterShow = false;
  discountPercent: string = '';
  routeCategory: string;
  filterProducts: Product[];

  constructor(
    private capstoreService: CapstoreService,
    private router: Router,
    private ar: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.ar.paramMap.subscribe((params) => {
      this.routeCategory = params.get('category');
    });

    this.getCategoryProducts(this.routeCategory);
  }

  getCategoryProducts(c) {
    this.category = this.routeCategory;

    this.capstoreService.getCategory(this.category).subscribe((response) => {
      this.categoryProducts = response;
    });
  }

  sortFilter(f, c) {
    this.filter = f.target.innerHTML;
    this.category = c;
    this.noShow = false;
    this.filterShow = true;
    this.capstoreService
      .sortFilter(this.filter, this.category)
      .subscribe((response) => {
        this.filterProducts = response;
      });
    console.log(this.filterProducts);
  }

  goToProductPage() {
    this.router.navigate(['productpage']);
  }
}
