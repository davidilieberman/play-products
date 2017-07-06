package controllers

import javax.inject.Inject

import models.Product
import play.api.i18n.{I18nSupport}
import play.api.mvc._

/**
  * Created by dlieberman on 7/5/17.
  */
class ProductsController @Inject()(cc: ControllerComponents) extends AbstractController(cc)
  with I18nSupport {

  def list = Action { implicit request: Request[AnyContent] =>
    val products = Product.findAll
    Ok(views.html.products.list(products))
  }

}
