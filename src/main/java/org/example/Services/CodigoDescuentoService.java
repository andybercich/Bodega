package org.example.Services;

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


    public boolean aplicarCodigo(CodigoDescuento codigo, Usuario usuario) {
        if (!codigo.isValid()) return false;

        switch (codigo.getTipoCodigo()) {
            case Publicitario:
                if (codigo.getUsuariosQueUsaron().contains(usuario))
                    return false;
                if (codigo.getLimiteUsado() <= 0)
                    return false;
                codigo.getUsuariosQueUsaron().add(usuario);
                codigo.setLimiteUsado(codigo.getLimiteUsado() - 1);
                break;
            case NuevosUsuarios:
                if (!usuario.usuarioNuevo())
                    return false;
                if (codigo.getUsuariosQueUsaron().contains(usuario))
                    return false;
                codigo.getUsuariosQueUsaron().add(usuario);
                break;
            case Personal:
                if (!codigo.getUsuarioAsignado().equals(usuario))
                    return false;
                if (codigo.getUsuariosQueUsaron().contains(usuario))
                    return false;
                codigo.getUsuariosQueUsaron().add(usuario);
                break;
        }

        repository.save(codigo);
        return true;
    }



}
