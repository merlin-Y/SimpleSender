import moe.tlaster.precompose.navigation.Navigator

object Navigator{
    var nav: Navigator? = null

    operator fun invoke(navigator: Navigator? = null): Navigator {
        if(nav == null){
            nav = navigator
        }
        return nav ?: throw IllegalStateException("Navigator is not initialized!")
    }
}