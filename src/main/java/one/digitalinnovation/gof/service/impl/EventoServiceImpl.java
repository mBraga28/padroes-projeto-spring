package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Evento;
import one.digitalinnovation.gof.model.EventoRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.EventoService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link EventoService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 */
@Service
public class EventoServiceImpl implements EventoService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private EventoRepository eventoRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Evento> buscarTodos() {
		// Buscar todos os eventos.
		return eventoRepository.findAll();
	}

	@Override
	public Evento buscarPorId(Long id) {
		// Buscar evento por ID.
		Optional<Evento> evento = eventoRepository.findById(id);
		return evento.get();
	}

	@Override
	public void inserir(Evento evento) {
		salvareventoComCep(evento);
	}

	@Override
	public void atualizar(Long id, Evento evento) {
		// Buscar evento por ID, caso exista:
		Optional<Evento> eventoBd = eventoRepository.findById(id);
		if (eventoBd.isPresent()) {
			salvareventoComCep(evento);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar evento por ID.
		eventoRepository.deleteById(id);
	}

	private void salvareventoComCep(Evento evento) {
		// Verificar se o Endereco do evento já existe (pelo CEP).
		String cep = evento.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		evento.setEndereco(endereco);
		// Inserir evento, vinculando o Endereco (novo ou existente).
		eventoRepository.save(evento);
	}

}
