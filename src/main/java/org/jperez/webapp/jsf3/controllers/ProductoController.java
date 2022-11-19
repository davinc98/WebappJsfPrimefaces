package org.jperez.webapp.jsf3.controllers;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jperez.webapp.jsf3.entities.Categoria;
import org.jperez.webapp.jsf3.entities.Producto;
import org.jperez.webapp.jsf3.services.ProductoService;

import java.util.List;
import java.util.ResourceBundle;


@Model
public class ProductoController {

    private Long id;

    private Producto producto;

    //    @Inject
    @ApplicationScoped
    private FacesContext facesContext;

    @Inject
    private ProductoService service;

    @Inject
    private ResourceBundle bundle;


    public ProductoController() {
        this.facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }


    @Produces
    @Model
    public String titulo() {
        return bundle.getString("producto.texto.titulo");
    }

    @Produces
    @RequestScoped
    @Named("listado")
    public List<Producto> findAll() {
        return service.listar();
    }

    @Produces
    @Model
    public Producto producto() {
        this.producto = new Producto();
        if (id != null && id > 0) {
            service.porId(id).ifPresent(p -> {
                this.producto = p;
            });
        }
        return producto;
    }

    @Produces
    @Model
    public List<Categoria> categorias() {
        return service.listarCategorias();
    }

    public String guardar() {
        System.out.println(producto);
        if (producto.getId() != null && producto.getId() > 0) {
            facesContext.addMessage(null,
                    new FacesMessage(String.format(bundle.getString("producto.mensaje.editar"), producto.getNombre())));
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(String.format(bundle.getString("producto.mensaje.crear"), producto.getNombre())));
        }
        service.guardar(producto);
        return "index.xhtml?faces-redirect=true";
    }

    public String editar(Long id) {
        this.id = id;
        return "form.xhtml";
    }

    public String eliminar(Producto prod) {
        service.eliminar(prod.getId());
        facesContext.addMessage(null,
                new FacesMessage(String.format(bundle.getString("producto.mensaje.eliminar"), prod.getNombre())));
        return "index.xhtml?faces-redirect=true";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
