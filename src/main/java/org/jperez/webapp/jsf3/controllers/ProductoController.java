package org.jperez.webapp.jsf3.controllers;


import jakarta.annotation.PostConstruct;
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

    private List<Producto> listado;

    private String textoBuscar;

    @PostConstruct
    public void init(){
        this.listado = service.listar();
    }

    public ProductoController() {
        this.facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }


    @Produces
    @Model
    public String titulo() {
        return bundle.getString("producto.texto.titulo");
    }

//    @Produces
//    @RequestScoped
//    @Named("listado")
//    public List<Producto> findAll() {
//        return service.listar();
//    }

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
        this.listado = service.listar();
        return "index.xhtml";
    }

    public String editar(Long id) {
        this.id = id;
        return "form.xhtml";
    }

    public void eliminar(Producto prod) {
        service.eliminar(prod.getId());
        facesContext.addMessage(null,
                new FacesMessage(String.format(bundle.getString("producto.mensaje.eliminar"), prod.getNombre())));
        this.listado = service.listar();
    }

    public void buscar(){
        this.listado = service.buscarPorNombre(this.textoBuscar);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Producto> getListado() {
        return listado;
    }

    public void setListado(List<Producto> listado) {
        this.listado = listado;
    }

    public String getTextoBuscar() {
        return textoBuscar;
    }

    public void setTextoBuscar(String textoBuscar) {
        this.textoBuscar = textoBuscar;
    }
}
