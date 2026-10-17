package fr.univ_amu.fr.formation.m1info.gla.storage.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.univ_amu.fr.formation.m1info.gla.data.formulas.CourseFormulaDTO;

public class FormulaCourseDTOXMLConvertor {
    String toXML(CourseFormulaDTO courseFormulaDTO){
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.writeValueAsString(courseFormulaDTO);
        }
        catch (Exception e) {
            throw new RuntimeException("Error converting FormulaCourseDTO to XML", e);
        }
    }

    CourseFormulaDTO fromXML(String xml){
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(xml, CourseFormulaDTO.class);
        }
        catch (Exception e) {
            throw new RuntimeException("Error converting XML to FormulaCourseDTO", e);
        }
    }
}
