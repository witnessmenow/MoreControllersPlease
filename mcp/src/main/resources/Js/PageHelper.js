
//Relies on a span element with a id of info
function checkForFocus()
{

        var info = document.getElementById ("info");
        if (!document.hasFocus ()) 
        {
            info.innerHTML = "Lost Focus - Please click on the page";
        }
        else
        {
        	info.innerHTML = "";
        }

}