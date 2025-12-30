package com.gtech.food_api.core.security.authorization;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.repository.UserRepository;

/**
 * Serviço de carregamento de usuários para autenticação Spring Security.
 * 
 * Esta classe implementa a interface UserDetailsService do Spring Security,
 * que é responsável por buscar informações do usuário durante o processo
 * de autenticação (login) no OAuth2 Authorization Server.
 * 
 * Fluxo de autenticação:
 * 1. Usuário tenta fazer login com email/senha no formulário
 * 2. Spring Security chama loadUserByUsername() passando o email informado
 * 3. Este método busca o usuário no banco de dados
 * 4. Se encontrado, retorna um UserDetails com email, senha e permissões
 * 5. Spring Security compara a senha informada com a senha armazenada (criptografada)
 * 6. Se as senhas coincidem, o usuário é autenticado
 * 
 * UserDetails: Interface do Spring Security que representa um usuário autenticado.
 * Contém informações essenciais: username, password, authorities (permissões), etc.
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true) // mantem a transação aberta para evitar bloqueios
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new org.springframework.security.core.userdetails.User(
            userEntity.getEmail(), 
            userEntity.getPassword(), 
            getAuthorities(userEntity)
        );
    }

    /**
     * Extrai todas as permissões (authorities) de um usuário através de seus grupos.
     * 
     * MODELO DE PERMISSÕES HIERÁRQUICO:
     * 
     * User (Usuário)
     *   └── Groups (Grupos)
     *         └── Permissions (Permissões)
     * IMPORTANTE: Este método é chamado durante o LOGIN e as permissões
     * são armazenadas no token JWT (claim "authorities") pelo jwtCustomizer.
     */
    private Collection<GrantedAuthority> getAuthorities(User user) {
        // PASSO 1: Para cada grupo, obter suas permissões e "achatar" em um único stream
            // flatMap transforma Stream<Group> em Stream<Permission>
            // 
            // SEM flatMap (estrutura aninhada):
            // [
            //   Group(Gerente) → [Permission(CONSULTAR_PEDIDOS), Permission(EDITAR_PEDIDOS)]
            //   Group(Vendas)  → [Permission(CONSULTAR_PEDIDOS), Permission(CRIAR_PEDIDOS)]
            // ]
            // COM flatMap (lista única de permissões):
            // [Permission(CONSULTAR_PEDIDOS), Permission(EDITAR_PEDIDOS), 
            //  Permission(CONSULTAR_PEDIDOS), Permission(CRIAR_PEDIDOS)]
            // PASSO 2: Converter cada Permission do domínio para GrantedAuthority do Spring Security
        return user.getGroups().stream()
            .flatMap(group -> group.getPermissions().stream())
            .map(permission -> new SimpleGrantedAuthority(permission.getName().toUpperCase()))
            .collect(Collectors.toSet());
    }
}
