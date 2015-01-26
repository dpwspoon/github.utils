package io.dpwspoon.github.utils;

import static io.dpwspoon.github.utils.RepoUtils.PUBLIC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public class RepoUtilsTest {

	List<String> listOfRepos = new ArrayList<String>();

	public RepoUtilsTest() {
		listOfRepos.add("REPOS YOU WANT TO IGNORE");
	}

	@Test
	@Ignore
	public void shouldPrintMyRepos() throws IOException {
		RepoUtils.printListOfRepos("dpwspoon", PUBLIC);
	}

	@Test
	@Ignore
	public void printListOfReposNotInListAndIsPublic() throws IOException {
		RepoUtils.printListOfRepos("kaazing",
				r -> !listOfRepos.contains(r.getName().replace("kaazing/", "")) && !r.isPrivate());
	}

	@Test
	@Ignore
	public void printListOfReposInListAndPrivate() throws IOException {
		RepoUtils.printListOfRepos("kaazing",
				r -> listOfRepos.contains(r.getName().replace("kaazing/", "")) && r.isPrivate());
	}

	@Test
	public void findIssuesBasedOnSearch() throws IOException {
		Consumer<GHRepository> consumer = new Consumer<GHRepository>() {

			@Override
			public void accept(GHRepository r) {
				PagedIterable<GHPullRequest> open = r.listPullRequests(GHIssueState.OPEN);
				PagedIterable<GHPullRequest> closed = r.listPullRequests(GHIssueState.CLOSED);
				Integer find = listRepos(open.iterator());
				if (find == null) {
					find = listRepos(closed.iterator());
				}
				if (find != null) {
					System.out.println("kaazing/" + r.getName() + "#" + find);
				}

			}

			private Integer listRepos(PagedIterator<GHPullRequest> pagedIterator) {
				while (pagedIterator.hasNext()) {
					GHPullRequest pullRequest = pagedIterator.next();
					String title = pullRequest.getTitle();
					if (title.contains("travis.yml")) {
						return pullRequest.getNumber();
					}
				}
				return null;
			}
		};
		RepoUtils.processRepositories("kaazing", PUBLIC, consumer);
	}

}
