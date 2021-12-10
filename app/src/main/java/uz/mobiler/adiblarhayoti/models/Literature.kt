package uz.mobiler.adiblarhayoti.models

import java.io.Serializable

class Literature :Serializable {

    var name:String?=null
    var birthYear:String?=null
    var dieYear:String?=null
    var type:String?=null
    var descriptions:String?=null
    var imageUrl:String?=null
    var like:Boolean?=null

    constructor()

    constructor(
        name: String?,
        birthYear: String?,
        dieYear: String?,
        type: String?,
        descriptions: String?,
        imageUrl: String?,
        like: Boolean?
    ) {
        this.name = name
        this.birthYear = birthYear
        this.dieYear = dieYear
        this.type = type
        this.descriptions = descriptions
        this.imageUrl = imageUrl
        this.like = like
    }


}