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
	
function drawImageAdjusted(ctx, img, x, y, scale)
{
	var xCo = x - scale/2;
	var yCo = y - scale/2;
		
	ctx.drawImage(img, xCo, yCo, scale, scale);
}