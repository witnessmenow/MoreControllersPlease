function addImageToCanvas(src, id, ctx, x, y, scale)
{
	var img = new Image();
	img.src = src;
	img.id = id;
	img.onload = function() 
	{
        drawImageAdjusted(ctx, img, x, y, scale)
    }
}
	
function clearCanvas(context)
{
	
	context.save();

	// Use the identity matrix while clearing the canvas
	context.setTransform(1, 0, 0, 1, 0, 0);
	context.clearRect(0, 0, canvas.width, canvas.height);

	// Restore the transform
	context.restore();
}

function drawImageAdjusted(ctx, img, x, y, scale)
{
	var xCo = x - scale/2;
	var yCo = y - scale/2;
		
	ctx.drawImage(img, xCo, yCo, scale, scale);
}