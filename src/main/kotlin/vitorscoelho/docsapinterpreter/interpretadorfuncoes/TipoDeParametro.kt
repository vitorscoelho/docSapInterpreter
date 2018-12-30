package vitorscoelho.docsapinterpreter.interpretadorfuncoes

enum class TipoDeParametro(
    val sapText: String,
    val kotlinText: String,
    val isEnum: Boolean,
    val enumClassName: String
) {
    STRING(sapText = "String", kotlinText = "String", isEnum = false, enumClassName = ""),
    BOOLEAN(sapText = "Boolean", kotlinText = "Boolean", isEnum = false, enumClassName = ""),
    DOUBLE(sapText = "Double", kotlinText = "Double", isEnum = false, enumClassName = ""),
    INT(sapText = "Long", kotlinText = "Int", isEnum = false, enumClassName = ""),
    UNITS(sapText = "eUnits", kotlinText = "Int", isEnum = true, enumClassName = "Units"),
    MAT_TYPE(sapText = "eMatType", kotlinText = "Int", isEnum = true, enumClassName = "MatType"),
    ITEM_TYPE(sapText = "eItemType", kotlinText = "Int", isEnum = true, enumClassName = "ItemType"),
    LOAD_PATTERN_TYPE(
        sapText = "eLoadPatternType",
        kotlinText = "Int",
        isEnum = true,
        enumClassName = "LoadPatternType"
    ),
    C_TYPE(sapText = "eCType", kotlinText = "Int", isEnum = true, enumClassName = "CType"),
    ITEM_TYPE_ELM(sapText = "eItemTypeElm", kotlinText = "Int", isEnum = true, enumClassName = "ItemTypeElm"),
    E2D_FRAME_TYPE(sapText = "e2DFrameType", kotlinText = "Int", isEnum = true, enumClassName = "E2DFrameType"),
    E3D_FRAME_TYPE(sapText = "e3DFrameType", kotlinText = "Int", isEnum = true, enumClassName = "E3DFrameType"),
    CONSTRAINT_TYPE(sapText = "eConstraintType", kotlinText = "Int", isEnum = true, enumClassName = "ConstraintType"),
    CONSTRAINT_AXIS(sapText = "eConstraintAxis", kotlinText = "Int", isEnum = true, enumClassName = "ConstraintAxis"),
    LOAD_CASE_TYPE(sapText = "eLoadCaseType", kotlinText = "Int", isEnum = true, enumClassName = "LoadCaseType"),
    FRAME_PROP_TYPE(sapText = "eFramePropType", kotlinText = "Int", isEnum = true, enumClassName = "FramePropType"),
    MAT_TYPE_STEEL(sapText = "eMatTypeSteel", kotlinText = "Int", isEnum = true, enumClassName = "MatTypeSteel"),
    MAT_TYPE_CONCRETE(
        sapText = "eMatTypeConcrete",
        kotlinText = "Int",
        isEnum = true,
        enumClassName = "MatTypeConcrete"
    ),
    MAT_TYPE_ALUMINUM(
        sapText = "eMatTypeAluminum",
        kotlinText = "Int",
        isEnum = true,
        enumClassName = "MatTypeAluminum"
    ),
    MAT_TYPE_COLD_FORMED(
        sapText = "eMatTypeColdFormed",
        kotlinText = "Int",
        isEnum = true,
        enumClassName = "MatTypeColdFormed"
    ),
    MAT_TYPE_REBAR(sapText = "eMatTypeRebar", kotlinText = "Int", isEnum = true, enumClassName = "MatTypeRebar"),
    MAT_TYPE_TENDON(sapText = "eMatTypeTendon", kotlinText = "Int", isEnum = true, enumClassName = "MatTypeTendon"),
    LINK_PROP_TYPES(sapText = "eLinkPropType", kotlinText = "Int", isEnum = true, enumClassName = "LinkPropType");

    companion object {
        private val map = TipoDeParametro.values().associate { it.sapText to it }

        fun get(text: String) =
            map.getOrElse(key = text) { throw IllegalArgumentException("ParameterType '$text' nonexistent.") }
    }
}