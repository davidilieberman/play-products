package controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, Action, ControllerComponents}

/**
  * Created by dlieberman on 7/7/17.
  */
class BarcodesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val ImageResolution = 144

  def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {
    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage
    import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
    import org.krysalis.barcode4j.impl.upcean.EAN13Bean

    val output = new ByteArrayOutputStream
    val canvas = new BitmapCanvasProvider(output, mimeType, ImageResolution,
      BufferedImage.TYPE_BYTE_BINARY, false, 0)
    val barcode = new EAN13Bean()
    barcode.generateBarcode(canvas, String valueOf ean)
    canvas.finish

    output.toByteArray
  }

  def barcode(ean: Long) = Action {
    val mimeType = "image/png"

    try {
      val imageData = ean13BarCode(ean, mimeType)
      Ok(imageData).as(mimeType)
    } catch {
      case e: IllegalArgumentException =>
        BadRequest("Couldn't generate bar code. Error " + e.getMessage)
    }
  }

}
