package org.jperez.webapp.jsf3.converters;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.convert.Converter;
import org.jperez.webapp.jsf3.entities.Categoria;
import org.jperez.webapp.jsf3.services.ProductoService;

import java.lang.annotation.Annotation;
import java.util.Optional;

@RequestScoped
@Named("categoriaConverter")
public class CategoriaConverter implements Converter<Categoria> {

    @Inject
    private ProductoService service;

    @Override
    public Categoria getAsObject(FacesContext context, UIComponent component, String id) {
        if(id==null)
            return null;

        Optional<Categoria> categoria = service.porIdCategoria(Long.valueOf(id));
        if(categoria.isPresent())
            return categoria.get();
        else
            return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Categoria categoria) {
        if(categoria==null)
            return "0";
        return categoria.getId().toString();
    }
}
