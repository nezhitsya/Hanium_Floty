package com.hanium.floty.model

import javax.xml.bind.annotation.*

//@Xml(name = "response")
//data class  Plant (
//
//        @Element
//        val header: Header,
//        @Element
//        val body: Body
//
//) {
//
//}
//
//@Xml(name = "header")
//data class Header(
//
//        @PropertyElement
//        val resultCode: Int,
//        @PropertyElement
//        val resultMsg: String
//
//)
//
//@Xml(name = "body")
//data class Body(
//
//        @Element
//        val items: Items,
//        @PropertyElement
//        val numOfRows: Int,
//        @PropertyElement
//        val pageNo: Int,
//        @PropertyElement
//        val totalCount: Int
//)
//
//@Xml
//data class Items(
//
//        @Element(name = "item")
//        val item: List<Item>
//
//)
//
//@Xml
//data class Item(
//
//        @PropertyElement(name = "cntntsNo")
//        var cntntsNo: String?,
//        // 식물 이름
//        @PropertyElement(name = "cntntsSj")
//        var cntntsSj: String?,
//        // http://www.nongsaro.go.kr/ + rtnFileCours + rtnStreFileNm = 이미지 경로
//        @PropertyElement(name = "rtnFileCours")
//        var rtnFileCours: String?,
//        @PropertyElement(name = "rtnStreFileNm")
//        var rtnStreFileNm: String?
//
//) {
//        constructor() : this(null, null,null, null)
//}

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
data class Plant(
//        @field:XmlElementWrapper(name = "header")
//        var header: Header,
//        @field:XmlElementWrapper(name = "body")
//        var body: Body
        @field:XmlElementWrapper(name = "body")
        @field:XmlElement(name = "items")
        var items: List<Item>
)

//@XmlRootElement(name = "header")
//@XmlAccessorType(XmlAccessType.FIELD)
//data class Header(
//        @field:XmlElementWrapper(name = "resultCode")
//        var resultCode: Int,
//        @field:XmlElementWrapper(name = "resultMsg")
//        var resultMsg: Int
//)
//
//@XmlRootElement(name = "body")
//@XmlAccessorType(XmlAccessType.FIELD)
//data class Body(
//        @field:XmlElementWrapper(name = "items")
//        val items: Items,
//        @field:XmlElementWrapper(name = "numOfRows")
//        val numOfRows: Int,
//        @field:XmlElementWrapper(name = "pageNo")
//        val pageNo: Int,
//        @field:XmlElementWrapper(name = "totalCount")
//        val totalCount: Int
//)
//
//@XmlRootElement(name = "items")
//@XmlAccessorType(XmlAccessType.FIELD)
//data class Items(
//
//        @field:XmlElementWrapper(name = "item")
//        val item: List<Item>
//
//)

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
data class Item(

//        @field:XmlElementWrapper(name = "cntntsNo")
//        var cntntsNo: String?,
//        // 식물 이름
//        @field:XmlElementWrapper(name = "cntntsSj")
//        var cntntsSj: String?,
//        // http://www.nongsaro.go.kr/ + rtnFileCours + rtnStreFileNm = 이미지 경로
//        @field:XmlElementWrapper(name = "rtnFileCours")
//        var rtnFileCours: String?,
//        @field:XmlElementWrapper(name = "rtnStreFileNm")
//        var rtnStreFileNm: String?
        var cntntsNo: String?,
        // 식물 이름
        var cntntsSj: String?,
        // http://www.nongsaro.go.kr/ + rtnFileCours + rtnStreFileNm = 이미지 경로
        var rtnFileCours: String?,
        var rtnStreFileNm: String?

) {
        constructor() : this(null, null,null, null)
}