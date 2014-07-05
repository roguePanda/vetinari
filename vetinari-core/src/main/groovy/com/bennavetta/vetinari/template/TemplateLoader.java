package com.bennavetta.vetinari.template;

import com.bennavetta.vetinari.VetinariContext;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.inject.Singleton;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.google.common.io.Files.getFileExtension;

/**
 * Loads various kinds of templates.
 */
@Singleton
public class TemplateLoader
{
	private final VetinariContext context;
	private final Set<TemplateEngine> templateEngines;
	private final TemplateEngine defaultTemplateEngine;

	private final LoadingCache<String, Template> templateCache;

	@Inject
	public TemplateLoader(VetinariContext context, Set<TemplateEngine> templateEngines)
	{
		this.context = context;
		this.templateEngines = templateEngines;
		this.defaultTemplateEngine = Iterables.find(templateEngines, t -> t.getName().equals(context.getSiteConfig().getString("defaultTemplateEngine")));

		this.templateCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Template>()
		{
			@Override
			public Template load(String templateName) throws Exception
			{
				final Path templatePath = context.getTemplateRoot().resolve(templateName);
				final String extension = getFileExtension(templateName);
				final TemplateEngine engine = Iterables.tryFind(templateEngines, t -> Iterables.contains(t.getFileExtensions(), extension)).or(defaultTemplateEngine);
				String source = new String(Files.readAllBytes(templatePath), context.getContentEncoding());
				return engine.compile(source);
			}
		});
	}

	public Template get(String path) throws IOException
	{
		try
		{
			return templateCache.get(path);
		}
		catch (ExecutionException e)
		{
			Throwables.propagateIfInstanceOf(e.getCause(), IOException.class);
			throw Throwables.propagate(e);
		}
	}
}
