package com.veriff.sdk.identity.live.mlkit.vision.face_detection

import android.graphics.*
import com.google.mlkit.vision.face.Face
import com.veriff.sdk.camerax.GraphicOverlay

/**
 * Face contour graphic
 *
 * @property face
 * @property imageRect
 * @constructor
 *
 * @param overlay
 */
class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint

    init {
        val selectedColor = Color.WHITE

        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor
        idPaint.textSize = ID_TEXT_SIZE

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    override fun draw(canvas: Canvas?) {
        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.boundingBox
        )
        canvas?.drawRect(rect, boxPaint)
    }

    companion object {
        private const val ID_TEXT_SIZE = 30.0f
        private const val BOX_STROKE_WIDTH = 5.0f
    }

}