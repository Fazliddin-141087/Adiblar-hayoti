package uz.mobiler.adiblarhayoti.models

import java.io.Serializable

class Literature :Serializable {

    var name:String?=null
    var years:String?=null
    var type:String?=null
    var descriptions:String?=null
    var imageUrl:String?=null
    var like:Boolean?=null

    constructor()

    constructor(
        name: String?,
        years: String?,
        type: String?,
        descriptions: String?,
        imageUrl: String?,
        like: Boolean?
    ) {
        this.name = name
        this.years = years
        this.type = type
        this.descriptions = descriptions
        this.imageUrl = imageUrl
        this.like = like
    }

}