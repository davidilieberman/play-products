package controllers

import javax.inject.Inject

import models.Product
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.api.mvc.Flash

import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, nonEmptyText}
import play.api.i18n.Messages

/**
  * Created by dlieberman on 7/5/17.
  */
class ProductsController @Inject()(config: Configuration,
                                   cc: ControllerComponents,
                                   listTemplate: views.html.products.list,
                                   mainTemplate: views.html.main,
                                   debugTemplate: views.html.debug,
                                   detailsTemplate: views.html.products.details,
                                   editTemplate: views.html.products.editProduct)
  extends AbstractController(cc)
    with I18nSupport {

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.duplicate", {Product.findByEan(_).isEmpty}),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def newProduct = Action { implicit request: Request[AnyContent]  =>
    val form = if (request.flash.get("error").isDefined)
      productForm.bind(request.flash.data)
    else
      productForm

    Ok(editTemplate(form))
  }

  def save = Action { implicit request: Request[AnyContent] =>
    val newProductForm = productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.ProductsController.newProduct())
            .flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
      },

      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.ProductsController.show(newProduct.ean))
            .flashing("success" -> message)
      }
    )
  }

  def list() = Action { implicit request: Request[AnyContent] =>
    val products = Product.findAll
    Ok(listTemplate(products))
  }

  def show(ean: Long) = Action { implicit request: Request[AnyContent] =>
    Product.findByEan(ean).map { product =>
      Ok(detailsTemplate(product))
    }.getOrElse(NotFound)

  }

}
