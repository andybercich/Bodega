package org.example.Services;

import org.example.Entities.AplicarCodigo;
import org.example.Entities.CodigoDescuento;
import org.example.Entities.Usuario;
import org.example.Repositories.CodigoDescuentoRepository;
import org.springframework.stereotype.Service;

@Service
public class CodigoDescuentoService extends BaseService<CodigoDescuento, Long, CodigoDescuentoRepository>{


    @Override
    public CodigoDescuento save(CodigoDescuento codigoDescuento){

        return repository.save(codigoDescuento);

    }


    public AplicarCodigo aplicarCodigo(String stringCodigo, Usuario usuario) {
        CodigoDescuento codigo =  repository.findByCodigo(stringCodigo);
        if (codigo == null){
            return new AplicarCodigo(false, 0);
        }
        if (!codigo.isValid()) return new AplicarCodigo(false, 0);

        switch (codigo.getTipoCodigo()) {
            case Publicitario:
                if (codigo.getUsuariosQueUsaron().contains(usuario))
                    return new AplicarCodigo(false, 0);
                if (codigo.getLimiteUsado() <= 0)
                    return new AplicarCodigo(false, 0);
                codigo.getUsuariosQueUsaron().add(usuario);
                codigo.setLimiteUsado(codigo.getLimiteUsado() - 1);
                break;
            case NuevosUsuarios:
                if (!usuario.usuarioNuevo())
                    return new AplicarCodigo(false, 0);
                if (codigo.getUsuariosQueUsaron().contains(usuario))
                    return new AplicarCodigo(false, 0);
                codigo.getUsuariosQueUsaron().add(usuario);
                break;
            case Personal:
                if (!codigo.getUsuarioAsignado().equals(usuario))
                    return new AplicarCodigo(false, 0);
                if (codigo.getUsuariosQueUsaron().contains(usuario))
                    return new AplicarCodigo(false, 0);
                codigo.getUsuariosQueUsaron().add(usuario);
                break;
        }

        repository.save(codigo);
        return new AplicarCodigo(true, codigo.getPorcentajeDescuento());
    }



}
