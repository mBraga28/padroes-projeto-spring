package one.digitalinnovation.gof.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.digitalinnovation.gof.model.Evento;
import one.digitalinnovation.gof.service.EventoService;

/**
 * Esse {@link RestController} representa nossa <b>Facade</b>, pois abstrai toda
 * a complexidade de integrações (Banco de Dados H2 e API do ViaCEP) em uma
 * interface simples e coesa (API REST).
 * 
 * @author falvojr
 */
@RestController
@RequestMapping("eventos")
public class EventoRestController {

	@Autowired
	private EventoService eventoService;

	@GetMapping
	public ResponseEntity<Iterable<Evento>> buscarTodos() {
		return ResponseEntity.ok(eventoService.buscarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Evento> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(eventoService.buscarPorId(id));
	}

	@PostMapping
	public ResponseEntity<Evento> inserir(@RequestBody Evento evento) {
		eventoService.inserir(evento);
		return ResponseEntity.ok(evento);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Evento> atualizar(@PathVariable Long id, @RequestBody Evento evento) {
		eventoService.atualizar(id, evento);
		return ResponseEntity.ok(evento);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		eventoService.deletar(id);
		return ResponseEntity.ok().build();
	}
}
