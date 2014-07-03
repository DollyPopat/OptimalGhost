package optimal.ghost.spring.controller;

import javax.servlet.http.HttpSession;

import optimal.ghost.dto.Response;
import optimal.ghost.game.GhostGame;
import optimal.ghost.game.GhostGame.InvalidPlay;
import optimal.ghost.tree.TreeManager.Dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping(value = "/ajaxgame")
public class AjaxGhostGameControler {

	private static final Logger logger = LoggerFactory.getLogger(AjaxGhostGameControler.class);

	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpSession httpSession, Model model) {
		logger.info("Accessing to Optimal Ghost!...");
		
		// In this case we have to set the response through Model...
		model.addAttribute("response", 
			new Response(getGhostGame(httpSession).getCurrentNode().getString(), ""));
		
		return "ajaxgame";
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response post(
			@RequestParam(value = "play", required = false) Character play,
			HttpSession httpSession) {
		logger.info("Received play [" + play + "] for session id =[" + httpSession.getId() + "] ");
		
		GhostGame ghostGame = getGhostGame(httpSession);
		try {
			ghostGame.playAgainstComputer(play);
			
			if (ghostGame.gameOver()) {
				httpSession.invalidate();
				String string = ghostGame.getCurrentNode().getString();
				return new Response(string, 
						"Game Over: Player " + ghostGame.getCurrentNode().whoPlaysThisNode() + " loses. [" 
						 + string + "] is a word in our dictionary ...");
			}
			
		} catch (InvalidPlay invalidPlay) {
			httpSession.invalidate();
			return new Response(ghostGame.getCurrentNode().getString(),
					"Game Over: " + invalidPlay.getMessage());
		}

		return new Response(ghostGame.getCurrentNode().getString(), "");
	}

	private GhostGame getGhostGame(HttpSession httpSession) {
		GhostGame ghostGame = (GhostGame) httpSession.getAttribute("game");

		if (ghostGame == null) {
			ghostGame = new GhostGame(Dictionary.DEFAULT);
			httpSession.setAttribute("game", ghostGame);
		}

		return ghostGame;
	}
}
