function Sprite(mainCanvas, src, imageWidth, imageHeight, offset, alph)
{
	this.alpha = alph || 1;
	
	this.off = offset;
	
	this.posX = 0;
	this.posY = 0;
	
	this.windowWidth = window.innerWidth;
	this.windowHeight = window.innerHeight;

	this.canvas = mainCanvas;
	this.context = this.canvas.getContext("2d");

	this.img = new Image();
	this.img.src = src;
	
	this.imgWidth = imageWidth;
	this.imgHeight = imageHeight;
	
}

Sprite.prototype.drawSprite = function() 
{
	this.context.save();

	this.context.globalAlpha = this.alpha;
	
	if(this.off)
	{
		this.context.drawImage(
			this.img,
			(this.posX * window.innerWidth) - this.imgWidth/2, (((this.posY * window.innerHeight)))  - this.imgHeight/2,
			this.imgWidth, this.imgHeight
		);
	}
	else
	{
		this.context.drawImage(
				this.img,
				this.posX * window.innerWidth, this.posY * window.innerHeight,
				this.imgWidth, this.imgHeight
			);
	}

	this.context.restore();
};

Sprite.prototype.setX = function(x)
{
	this.posX = x;
}

Sprite.prototype.setY = function(y)
{
	this.posY = y;
}