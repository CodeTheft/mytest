package com.my.test.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ElasticSearch客户端demo
 * 
 * @author chufei
 * @date 2017年11月6日
 */
public class ElasticSearchClient {

	public static void main(String[] args) {
		TransportClient client = null;
		try {
			client = NoClusterTransportClient();
			// indexDocument(client);
			// get(client);
			// delete(client);
			// update(client);
			// multiGet(client);
			// bulk(client);
			search(client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	// 创建索引
	private static void indexDocument(TransportClient client) {
		IndexResponse response = client.prepareIndex("facebook", "fb", "1").setSource(getJSON_XContentBuilder()).get();

		// IndexResponse response = client.prepareIndex("twitter",
		// "tweet").setSource(getJSON_String(), XContentType.JSON).get();

		String _index = response.getIndex();
		System.out.println("index name:" + _index);
		String _type = response.getType();
		System.out.println("type name:" + _type);
		String _id = response.getId();
		System.out.println("document id:" + _id);
		long _version = response.getVersion();
		System.out.println("version:" + _version);
		RestStatus restStatus = response.status();
		System.out.println("restStatus:" + restStatus.toString());
	}

	// 获取索引
	private static void get(TransportClient client) {
		GetResponse response = client.prepareGet("google", "go", "404").setOperationThreaded(false).get();

		String _index = response.getIndex();
		System.out.println("index name:" + _index);
		String _type = response.getType();
		System.out.println("type name:" + _type);
		String _id = response.getId();
		System.out.println("document id:" + _id);
		long _version = response.getVersion();
		System.out.println("version:" + _version);
		Map<String, Object> fields = response.getSource();
		if (fields != null) {
			System.out.println("fields:" + fields.toString());
		}
	}

	// 删除索引
	private static void delete(TransportClient client) {
		DeleteResponse response = client.prepareDelete("facebook", "fb", "1").get();

		System.out.println("restStatus:" + response.status());
	}

	// 更新索引
	private static void update(TransportClient client) {
		try {
			UpdateResponse response = client.prepareUpdate("facebook", "fb", "1")
					.setDoc(XContentFactory.jsonBuilder().startObject().field("user", "do it").endObject()).get();

			System.out.println("restStatus:" + response.status());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 多条索引查询
	private static void multiGet(TransportClient client) {
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet().add("facebook", "fb", "1")
				.add("twitter", "tweet", "2").get();

		for (MultiGetItemResponse multiGetItemResponse : multiGetItemResponses) {
			GetResponse response = multiGetItemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
				System.out.println(json);
			}
		}
	}

	// 合并多条操作
	private static void bulk(TransportClient client) {
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			bulkRequest.add(client.prepareIndex("google", "go", "404")
					.setSource(XContentFactory.jsonBuilder().startObject().field("user", "bule")
							.field("postDate", new Date()).field("message", "trying out Elasticsearch").endObject()));
			bulkRequest.add(client.prepareIndex("google", "go", "505")
					.setSource(XContentFactory.jsonBuilder().startObject().field("user", "luck")
							.field("postDate", new Date()).field("message", "another post").endObject()));

			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
				// process failures by iterating through each bulk response item
				System.out.println("fail message: " + bulkResponse.buildFailureMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void bulkProcessor(TransportClient client) {
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
			public void beforeBulk(long executionId, BulkRequest request) {
				// TODO bulk执行之前

			}

			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				// TODO bulk执行抛出异常之后

			}

			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				// TODO bulk成功执行之后

			}
		}).setBulkActions(10000) // 每10000个请求执行一次，默认1000
				.setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB)) // 每5M刷新一次，默认5MB
				.setFlushInterval(TimeValue.timeValueSeconds(5)) // 无论请求数量多少，都每5秒刷新一次，默认不刷新
				.setConcurrentRequests(1) // 设置并发请求的数量。值为0意味着只有一个请求将被允许执行。值为1表示0在累积新的批量请求时允许执行1个并发请求。默认1
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) // 设置一个自定义退避策略，最初等待100ms，成倍增长，重试三次。每当有一个或多个批量项目请求失败时，就会尝试重试，EsRejectedExecutionException
																										// 这表示计算资源可用于处理请求的计算资源太少。要禁用退避，请传递BackoffPolicy.noBackoff()。
																										// 默认将退避策略设置为具有8次重试的指数退避和50ms的启动延迟。总的等待时间大约是5.1秒。
				.build();

		// 添加请求
		bulkProcessor.add(new IndexRequest());
		// 刷新任何剩余请求
		bulkProcessor.flush();
		// 关闭请求
		bulkProcessor.close();// or bulkProcessor.awaitClose(10000,
								// TimeUnit.MILLISECONDS);
		// 刷新索引
		client.admin().indices().prepareRefresh().get();
		// 搜索索引
		client.prepareSearch().get();
	}

	// 搜索索引，可跨索引查询
	private static void search(TransportClient client) {
		SearchResponse response = client.prepareSearch("facebook", "google").setTypes("fb", "go")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				// .setQuery(QueryBuilders.termQuery("user", "k"))
				.setFrom(0).setSize(10).setExplain(true).get();

		System.out.println(response.toString());
	}

	// 滚动搜索
	private static void scroll(TransportClient client) {
		SearchResponse scrollResp = client.prepareSearch().addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
				.setScroll(new TimeValue(60000)).setSize(100).get();
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				// Handle the hit...
			}

			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute()
					.actionGet();
		} while (scrollResp.getHits().getHits().length != 0);
	}

	@SuppressWarnings("unused")
	private static TransportClient ClusterTransportClient() {
		// 采用集群，必须为集群设置名称
		try {
			Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
					.put("client.transport.sniff", true).build();// 启动自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中，默认5s刷新
			/*
			 * client.transport.ignore_cluster_name，设置为true忽略连接节点的群集名称验证。
			 * client.transport.ping_timeout，等待来自节点的ping响应的时间。默认为5s。
			 * client.transport.nodes_sampler_interval，采样/ping所列出和连接的节点的频率。
			 * 默认为5s。
			 */
			@SuppressWarnings("unchecked")
			TransportClient client = new PreBuiltTransportClient(settings);
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

			return client;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "resource" })
	private static TransportClient NoClusterTransportClient() {
		// 不加入集群，在对多台地址之间轮询通信
		try {
			TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			// .addTransportAddress(new
			// InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),
			// 9300));

			// client.close();
			return client;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unused")
	private static String getJSON_String() {
		String json_String = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\","
				+ "\"message\":\"trying out Elasticsearch\"" + "}";
		return json_String;
	}

	@SuppressWarnings("unused")
	private static Map<String, Object> getJSON_Map() {
		Map<String, Object> json_Map = new HashMap<String, Object>();
		json_Map.put("user", "kimchy");
		json_Map.put("postDate", new Date());
		json_Map.put("message", "trying out Elasticsearch");
		return json_Map;
	}

	@SuppressWarnings("unused")
	private static byte[] getJSON_Lib() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonBean bean = new JsonBean();
			bean.setUser("kimchy");
			bean.setPostDate(new Date());
			bean.setMessage("trying out Elasticsearch");
			byte[] json_JSONLib = mapper.writeValueAsBytes(bean);
			return json_JSONLib;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "".getBytes();
		}
	}

	private static XContentBuilder getJSON_XContentBuilder() {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("user", "code")
					.field("postDate", new Date()).field("message", "Elasticsearch demo").endObject();
			return builder;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static class JsonBean {
		private String user;
		private Date postDate;
		private String message;

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public Date getPostDate() {
			return postDate;
		}

		public void setPostDate(Date postDate) {
			this.postDate = postDate;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

}
