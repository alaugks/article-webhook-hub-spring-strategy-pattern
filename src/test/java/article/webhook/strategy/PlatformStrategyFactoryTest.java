package article.webhook.strategy;

import article.webhook.fixtures.TestStrategyDummy;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PlatformStrategyFactoryTest {

	private PlatformStrategyFactory strategyFactory;

	@BeforeEach
	void beforeEach() {
		this.strategyFactory = new PlatformStrategyFactory(List.of(new TestStrategyDummy()));
	}

	@Test
	void test_getPlatformStrategy() {
		assertThat(this.strategyFactory.getPlatformStrategy("foobar")).isInstanceOf(TestStrategyDummy.class);
	}

	@Test
	void test_getPlatformStrategy_not_found() {
		assertThat(this.strategyFactory.getPlatformStrategy("dummy")).isNull();
	}
}
